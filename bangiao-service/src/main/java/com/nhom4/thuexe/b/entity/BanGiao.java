package com.nhom4.thuexe.bangiao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ban_giao")
public class BanGiao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hopDongId;
    private String trangThai; // BAN_GIAO_THANH_CONG hoặc TU_CHOI
    private String lyDoTuChoi;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getHopDongId() { return hopDongId; }
    public void setHopDongId(Long hopDongId) { this.hopDongId = hopDongId; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getLyDoTuChoi() { return lyDoTuChoi; }
    public void setLyDoTuChoi(String lyDoTuChoi) { this.lyDoTuChoi = lyDoTuChoi; }
}