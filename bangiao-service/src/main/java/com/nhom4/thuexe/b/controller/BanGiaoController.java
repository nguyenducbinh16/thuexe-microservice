package com.nhom4.thuexe.bangiao.controller;

import com.nhom4.thuexe.bangiao.entity.BanGiao;
import com.nhom4.thuexe.bangiao.repository.BanGiaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bangiao")
public class BanGiaoController {

    private static final Logger log = LoggerFactory.getLogger(BanGiaoController.class);

    // Biến điều khiển: true = bật từ chối
    private boolean batTuChoi = false;

    @Autowired
    private BanGiaoRepository banGiaoRepository;

    // API bật/tắt từ chối
    @PostMapping("/toggle-tu-choi")
    public ResponseEntity<?> toggleTuChoi() {
        batTuChoi = !batTuChoi;
        log.info("Trang thai tu choi: {}", batTuChoi);
        return ResponseEntity.ok("Tu choi = " + batTuChoi);
    }

    // Bước 4: lập phiếu bàn giao hoặc từ chối
    @PostMapping
    public ResponseEntity<?> lapPhieu(@RequestParam Long hopDongId, @RequestParam Long xeId) {
        BanGiao bg = new BanGiao();
        bg.setHopDongId(hopDongId);

        if (batTuChoi) {
            // TỪ CHỐI: log WARN không kèm exception — đây là NGUYÊN NHÂN GỐC
            String lyDo = "Xe chua qua kiem dinh";
            log.warn("Tu choi ban giao hop dong {}: {}", hopDongId, lyDo);
            bg.setTrangThai("TU_CHOI");
            bg.setLyDoTuChoi(lyDo);
            banGiaoRepository.save(bg);
            return ResponseEntity.ok("TU_CHOI:" + lyDo);
        }

        // THÀNH CÔNG
        log.info("Ban giao thanh cong hop dong {}", hopDongId);
        bg.setTrangThai("BAN_GIAO_THANH_CONG");
        banGiaoRepository.save(bg);
        return ResponseEntity.ok("BAN_GIAO_THANH_CONG");
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("OK");
    }
}