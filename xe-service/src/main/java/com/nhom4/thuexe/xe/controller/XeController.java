package com.nhom4.thuexe.xe.controller;

import com.nhom4.thuexe.xe.entity.Xe;
import com.nhom4.thuexe.xe.repository.XeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/xe")
public class XeController {

    private static final Logger log = LoggerFactory.getLogger(XeController.class);

    @Autowired
    private XeRepository xeRepository;

    // API giữ xe (bước 2 trong luồng 6 bước)
    @PostMapping("/{id}/giu")
    public ResponseEntity<?> giuXe(@PathVariable Long id) {
        Optional<Xe> opt = xeRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body("Khong tim thay xe");
        }
        Xe xe = opt.get();
        if (!xe.isDaKiemDinh()) {
            log.warn("Xe {} chua qua kiem dinh, khong the giu", id);
            return ResponseEntity.ok().body("XE_CHUA_KIEM_DINH");
        }
        if (xe.isDangChoThue()) {
            return ResponseEntity.ok().body("XE_DANG_DUOC_THUE");
        }
        xe.setDangChoThue(true);
        xeRepository.save(xe);
        log.info("Da giu xe {}", id);
        return ResponseEntity.ok("GIU_THANH_CONG");
    }

    // API trả xe (dùng khi hủy)
    @PostMapping("/{id}/tra")
    public ResponseEntity<?> traXe(@PathVariable Long id) {
        Optional<Xe> opt = xeRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.badRequest().body("Khong tim thay xe");
        }
        Xe xe = opt.get();
        xe.setDangChoThue(false);
        xeRepository.save(xe);
        log.info("Da tra xe {}", id);
        return ResponseEntity.ok("TRA_THANH_CONG");
    }

    // API tạo dữ liệu mẫu
    @PostMapping("/sample")
    public ResponseEntity<?> taoSample() {
        Xe xe1 = new Xe();
        xe1.setBienSo("51A-12345");
        xe1.setHangXe("Toyota Vios");
        xe1.setDaKiemDinh(true);
        xe1.setDangChoThue(false);
        xeRepository.save(xe1);

        Xe xe2 = new Xe();
        xe2.setBienSo("51B-67890");
        xe2.setHangXe("Honda City");
        xe2.setDaKiemDinh(false); // xe chưa kiểm định
        xe2.setDangChoThue(false);
        xeRepository.save(xe2);

        return ResponseEntity.ok("Da tao 2 xe mau");
    }

    // API kiểm tra trạng thái
    @GetMapping("/{id}")
    public ResponseEntity<?> getXe(@PathVariable Long id) {
        return xeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}