package com.nhom4.thuexe.hopdong.controller;

import com.nhom4.thuexe.hopdong.entity.HopDong;
import com.nhom4.thuexe.hopdong.repository.HopDongRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/hopdong")
public class HopDongController {

    private static final Logger log = LoggerFactory.getLogger(HopDongController.class);

    @Autowired
    private HopDongRepository hopDongRepository;

    @Value("${service.xe.url}")
    private String xeUrl;

    @Value("${service.khach.url}")
    private String khachUrl;

    @Value("${service.bangiao.url}")
    private String bangiaoUrl;

    private final RestClient restClient = RestClient.create();

    @PostMapping
    public ResponseEntity<?> datXe(@RequestParam Long khachId, @RequestParam Long xeId) {
        // Bước 1: tạo hợp đồng PENDING
        HopDong hd = new HopDong();
        hd.setKhachId(khachId);
        hd.setXeId(xeId);
        hd.setTrangThai("PENDING");
        hopDongRepository.save(hd);

        // Bước 2: giữ xe
        String kqGiuXe;
        try {
            kqGiuXe = restClient.post()
                    .uri(xeUrl + "/xe/" + xeId + "/giu")
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            log.warn("Goi xe-service that bai", e);
            hd.setTrangThai("CANCELLED");
            hd.setLyDoHuy("LOI_GOI_XE_SERVICE");
            hopDongRepository.save(hd);
            return ResponseEntity.ok(hd);
        }

        if (!"GIU_THANH_CONG".equals(kqGiuXe)) {
            log.warn("Khong the giu xe: {}", kqGiuXe);
            hd.setTrangThai("CANCELLED");
            hd.setLyDoHuy(kqGiuXe);
            hopDongRepository.save(hd);
            return ResponseEntity.ok(hd);
        }

        // Bước 3: trừ cọc
        String kqTruCoc;
        try {
            kqTruCoc = restClient.post()
                    .uri(khachUrl + "/khach/" + khachId + "/tru-coc")
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            log.warn("Goi khach-service that bai", e);
            hoanXe(xeId);
            hd.setTrangThai("CANCELLED");
            hd.setLyDoHuy("LOI_GOI_KHACH_SERVICE");
            hopDongRepository.save(hd);
            return ResponseEntity.ok(hd);
        }

        if (!"TRU_COC_THANH_CONG".equals(kqTruCoc)) {
            log.warn("Tru coc that bai: {}", kqTruCoc);
            hoanXe(xeId);
            hd.setTrangThai("CANCELLED");
            hd.setLyDoHuy(kqTruCoc);
            hopDongRepository.save(hd);
            return ResponseEntity.ok(hd);
        }

        // Bước 4: bàn giao
        String kqBanGiao;
        try {
            kqBanGiao = restClient.post()
                    .uri(bangiaoUrl + "/bangiao?hopDongId=" + hd.getId() + "&xeId=" + xeId)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException e) {
            // HẬU QUẢ: log WARN CÓ exception — đây là hậu quả, không phải nguyên nhân gốc
            log.warn("Goi ban giao-service that bai", e);
            hoanTienVaXe(khachId, xeId);
            hd.setTrangThai("CANCELLED");
            hd.setLyDoHuy("LOI_GOI_BAN_GIAO");
            hopDongRepository.save(hd);
            return ResponseEntity.ok(hd);
        }

        // Bước 5+6: xử lý kết quả
        if (kqBanGiao != null && kqBanGiao.startsWith("TU_CHOI")) {
            // Bị từ chối → hoàn tiền và trả xe
            hoanTienVaXe(khachId, xeId);
            hd.setTrangThai("CANCELLED");
            hd.setLyDoHuy(kqBanGiao);
            hopDongRepository.save(hd);
            log.warn("Hop dong {} bi huy do ban giao tu choi", hd.getId());
            return ResponseEntity.ok(hd);
        }

        // Thành công
        hd.setTrangThai("CONFIRMED");
        hopDongRepository.save(hd);
        log.info("Hop dong {} thanh cong", hd.getId());
        return ResponseEntity.ok(hd);
    }

    private void hoanXe(Long xeId) {
        try {
            restClient.post().uri(xeUrl + "/xe/" + xeId + "/tra").retrieve().body(String.class);
        } catch (RestClientException e) {
            log.warn("Hoan xe that bai", e);
        }
    }

    private void hoanTienVaXe(Long khachId, Long xeId) {
        try {
            restClient.post().uri(khachUrl + "/khach/" + khachId + "/hoan-coc").retrieve().body(String.class);
        } catch (RestClientException e) {
            log.warn("Hoan coc that bai", e);
        }
        hoanXe(xeId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHopDong(@PathVariable Long id) {
        return hopDongRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}