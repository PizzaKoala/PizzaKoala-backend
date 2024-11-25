package com.PizzaKoala.Pizza.member.repository;

import com.PizzaKoala.Pizza.member.entity.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImage,Long> {


}
