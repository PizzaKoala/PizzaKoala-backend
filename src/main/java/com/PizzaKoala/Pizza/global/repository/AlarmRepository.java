package com.PizzaKoala.Pizza.global.repository;

import com.PizzaKoala.Pizza.global.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Long> {

    Page<Alarm> findAllByReceiverId(Pageable pageable, Long memberId);

    void deleteAllByReceiverId(Long receiverId);
}
