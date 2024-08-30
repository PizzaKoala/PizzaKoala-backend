package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/calendar")
public class CalenderController {
    private final CalendarService calendarService;

    /**
     * Calendar - 캘린더 가져오기
     */
    //yearly calender
    @GetMapping("/{year}/{memberId}")
    public Response<List<LocalDate>> getYearlyCalendar(@PathVariable int year, @PathVariable Long memberId) {
        if (year > LocalDateTime.now().getYear() || year < 2024) {
            throw new PizzaAppException(ErrorCode.INVALID_YEAR);
        }
        return Response.success(calendarService.getPostsByYear(year,memberId));
    }

    //monthly calendar
    @GetMapping("/{year}/{month}/{memberId}")
    public List<LocalDate> getMonthlyCalendar(@PathVariable int year, @PathVariable int month,@PathVariable Long memberId) {

        if (year > LocalDateTime.now().getYear() || year < 2024) {
            throw new PizzaAppException(ErrorCode.INVALID_YEAR);
        }
        if (month > 12 || month<1) {
            throw new PizzaAppException(ErrorCode.INVALID_MONTH);
        }
        return calendarService.getPostsByMonth(year, month, memberId);
    }
}
