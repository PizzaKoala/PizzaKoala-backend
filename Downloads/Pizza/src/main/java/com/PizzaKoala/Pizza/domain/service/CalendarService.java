package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.CustomPostRepositoryImpl;
import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.PostRepository;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CustomPostRepositoryImpl customPostRepository;
    private final MemberRepository memberRepository;
//    public List<Void> getPostsByYear(int year) {
//        LocalDate startDate = LocalDate.of(year, 1, 1);
//        LocalDate endDate= LocalDate.of(year,12,31);
//        postRepository.
//    }

    public List<LocalDate> getPostsByMonth(int year, int month, Long memberId) {
       Optional<Member> member=memberRepository.findById(memberId);
        if (member.isEmpty() || member.get().getDeletedAt() != null) {
            throw new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND);
        }
        LocalDate startDate = LocalDate.of(year, month, 1);

        // End date is the last day of the month
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return customPostRepository.getPostsbyMonth(startDate,endDate,memberId);
    }
    public List<LocalDate> getPostsByYear(int year, Long memberId) {
       Optional<Member> member=memberRepository.findById(memberId);
        if (member.isEmpty() || member.get().getDeletedAt() != null) {
            throw new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND);
        }
        LocalDate startDate = LocalDate.of(year, 1, 1);

        // End date is the last day of the month

        LocalDate endDate = LocalDate.now();

        return customPostRepository.getPostsbyMonth(startDate,endDate,memberId);
    }


}
