package com.kg.yildizname.feature.calendar.ui

import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.HoroscopeRepository
import com.kg.yildizname.core.data.repository.UserRepository
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.feature.calendar.ui.components.nextMonth
import com.kg.yildizname.feature.calendar.ui.components.previousMonth
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.number
import kotlinx.datetime.plus

class CalendarViewModel(
    private val horoscopeRepository: HoroscopeRepository,
    private val userRepository: UserRepository
) : ViewModel()
{
    private val _uiState = MutableStateFlow<CalendarUiState>(CalendarUiState.Loading)
    val uiState : StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private var sign: ZodiacSign? = null
    private var lastFetchedData : String? = null

    private var dailyJob: Job? = null
    private var monthlyJob: Job? = null
    init {
        println("CalendarViewModel: init, loading monthly reading for ${DateUtils.todayLocalDate()}")
        loadMonthlyReading(DateUtils.todayLocalDate())
    }

    private fun loadMonthlyReading(month: LocalDate)
    {
        monthlyJob?.cancel()
        monthlyJob = viewModelScope.launch {
            val existing = _uiState.value as? CalendarUiState.Success
            if(existing == null)
                _uiState.value = CalendarUiState.Loading
            try
            {
                sign = userRepository.getSavedSign() ?: ZodiacSign.SCORPIO
                println("SIGN MONTHLY = $sign")
                horoscopeRepository.getReading(sign ?: ZodiacSign.SCORPIO, period = PeriodType.MONTHLY, monthlyKey(month))
                    .catch {
                        e ->
                        if(existing == null)
                        {
                            _uiState.value = CalendarUiState.Error(e.message ?: "An error occured.")
                        }
                        /***
                         * else show old months data.
                         */
                    }
                    .collect {
                        reading ->
                        val base = _uiState.value as? CalendarUiState.Success
                        _uiState.value = base?.copy(
                            date = month,
                            monthlyReading = reading,
                            selectedTab = PageTab.MONTHLY
                        ) ?: CalendarUiState.Success(
                            date = month,
                            selectedDay = null,
                            selectedTab = PageTab.MONTHLY,
                            luckDays = emptyList(),
                            dailyReading = null,
                            monthlyReading = reading
                        )
                    }
            }
            catch (
               e: Exception
            )
            {
                if(existing == null)
                _uiState.value = CalendarUiState.Error(e.message ?: "error")
            }
        }
    }

    private fun loadDailyReading(date: LocalDate)
    {
        dailyJob?.cancel()
        dailyJob = viewModelScope.launch {
            val existing = _uiState.value as? CalendarUiState.Success
            if(existing == null)
                _uiState.value = CalendarUiState.Loading
            try {
                val resolvedSign = sign ?: (userRepository.getSavedSign() ?: ZodiacSign.SCORPIO).also { sign = it }
                horoscopeRepository.getReading(resolvedSign, period = PeriodType.DAILY,date = dailyKey(date))
                    .catch {
                        if(existing == null)
                        {
                            _uiState.value = CalendarUiState.Error(it.message ?: "error")
                        }
                    }
                    .collect {
                        reading ->
                        val base = _uiState.value as? CalendarUiState.Success ?: return@collect
                        _uiState.value = base.copy(
                            dailyReading = reading
                        )
                    }
            }
            catch (e : Exception)
            {
                if(existing == null)
                    _uiState.value = CalendarUiState.Error(e.message ?: "error")
            }
        }
    }
    fun onNextMonth()
    {
        val current = _uiState.value as? CalendarUiState.Success ?: return
        val newMonth = current.date.plus(DatePeriod(months = 1))
        _uiState.value = current.copy(
            date = newMonth,
            selectedDay = null,
            selectedTab = PageTab.MONTHLY,
            monthlyReading = null,
            dailyReading = null
        )
        if(!isFutureMonth(newMonth))
        {
            loadMonthlyReading(newMonth)
        }
    }
    fun onPreviousMonth() {
        val current = _uiState.value as? CalendarUiState.Success ?: return
        val newMonth = current.date.minus(DatePeriod(months = 1))
        if (newMonth < DateUtils.earliestAvailableDate) return
        _uiState.value = current.copy(date = newMonth, selectedDay = null, selectedTab = PageTab.MONTHLY, monthlyReading = null, dailyReading = null)
        loadMonthlyReading(newMonth)
    }
    private fun isFutureMonth(month: LocalDate): Boolean {
        val today = DateUtils.todayLocalDate()
        return month.year > today.year ||
                (month.year == today.year && month.month.number > today.month.number)
    }
    fun onDaySelected(day: CalendarDay)
    {
        val current = _uiState.value as? CalendarUiState.Success ?: return
        val resolvedDate = resolveDate(day,current.date)
        _uiState.value = current.copy(
            selectedDay = day,
            selectedTab = PageTab.DAILY
        )
        if(day.isAvailable)
        {
            loadDailyReading(resolvedDate)
        }
    }
    fun onTabChange(pageTab: PageTab)
    {
        val current = _uiState.value as? CalendarUiState.Success ?: return
        if(pageTab == PageTab.MONTHLY)
        {
            // switch to monthly
            _uiState.value = current.copy(selectedTab = pageTab , selectedDay = null)
        }
        else {
            val today = DateUtils.todayLocalDate()
            val isViewingCurrentMonth = current.date.year == today.year &&
                    current.date.month.number == today.month.number

            if (current.selectedDay == null && isViewingCurrentMonth) {
                val todayAsDay = CalendarDay(day = today.day, relation = MonthRelation.CURRENT, isAvailable = true)
                _uiState.value = current.copy(selectedDay = todayAsDay, selectedTab = pageTab)
                loadDailyReading(today)
            } else {
                _uiState.value = current.copy(selectedTab = pageTab)
                // if selectedDay is still null here, the placeholder card shows — correct, no mismatch possible
            }
        }
    }

    fun clearDay()
    {
        val current = _uiState.value as? CalendarUiState.Success ?: return
        _uiState.value = current.copy(
            selectedDay = null,
            dailyReading = null
        )
    }
    private fun dailyKey(date: LocalDate) : String = date.toString()

    private fun resolveDate(day: CalendarDay, displayedMonth: LocalDate): LocalDate {
        val monthAnchor = when (day.relation) {
            MonthRelation.PREVIOUS -> displayedMonth.previousMonth()
            MonthRelation.CURRENT -> displayedMonth
            MonthRelation.NEXT -> displayedMonth.nextMonth()
        }
        return LocalDate(monthAnchor.year, monthAnchor.month, day.day)
    }

    private fun monthlyKey(date: LocalDate) : String = "${date.year}-${date.month.number.toString().padStart(2,'0')}"
}