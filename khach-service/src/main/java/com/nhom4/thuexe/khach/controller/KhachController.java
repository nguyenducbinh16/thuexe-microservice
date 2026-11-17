package com.nhom4.thuexe.khach.controller;

import com.nhom4.thuexe.khach.entity.Khach;
import com.nhom4.thuexe.khach.repository.KhachRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/khach")
public class KhachController {

    private static final Logger log = LoggerFactory.getLogger(KhachController.class);
    private static final BigDecimal TIEN_COC = new BigDecimal("500000");

    @Autowired
    private KhachRepository khachRepository;

    // Bước 3: trừ tiền cọc
    @PostMapping("/{id}/tru-coc")
    public ResponseEntity<?> truCoc(@PathVariable Long id) {
        Optional<Khach> opt = khachRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body("KHONG_TIM_THAY_KHACH");
        }
        Khach khach = opt.get();
        if (khach.getSoDuVi().compareTo(TIEN_COC) < 0) {
            log.warn("Khach {} khong du tien coc", id);
            return ResponseEntity.ok("KHONG_DU_TIEN");
        }
        khach.setSoDuVi(khach.getSoDuVi().subtract(TIEN_COC));
        khachRepository.save(khach);
        log.info("Da tru coc khach {}, so du con {}", id, khach.getSoDuVi());
        return ResponseEntity.ok("TRU_COC_THANH_CONG");
    }

    // Bước 5: hoàn tiền nếu bị từ chối
    @PostMapping("/{id}/hoan-coc")
    public ResponseEntity<?> hoanCoc(@PathVariable Long id) {
        Optional<Khach> opt = khachRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body("KHONG_TIM_THAY_KHACH");
        }
        Khach khach = opt.get();
        khach.setSoDuVi(khach.getSoDuVi().add(TIEN_COC));
        khachRepository.save(khach);
        log.info("Da hoan coc khach {}, so du moi {}", id, khach.getSoDuVi());
        return ResponseEntity.ok("HOAN_COC_THANH_CONG");
    }

    // Tạo dữ liệu mẫu
    @PostMapping("/sample")
    public ResponseEntity<?> taoSample() {
        Khach k = new Khach();
        k.setHoTen("Nguyen Van A");
        k.setSoDuVi(new BigDecimal("2000000"));
        khachRepository.save(k);
        return ResponseEntity.ok("Da tao khach mau id=" + k.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getKhach(@PathVariable Long id) {
        return khachRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}