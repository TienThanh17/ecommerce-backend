package com.bamito.repository;

import com.bamito.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface IVoucherRepository extends JpaRepository<Voucher, String> {
    Page<Voucher> findAll(Pageable pageable);

    Page<Voucher> findAllByIdContaining(String id, Pageable pageable);

    @Query("select v from Voucher v where " +
            "v.quantity > 0 and " +
            "function('FROM_UNIXTIME', cast(v.endTime AS long) / 1000) > current_timestamp ")
    Set<Voucher> findAllVouchers();
}

