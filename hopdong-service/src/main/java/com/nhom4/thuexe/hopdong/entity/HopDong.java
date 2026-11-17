package com.nhom4.thuexe.hopdong.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hop_dong")
public class HopDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long khachId;
    private Long xeId;
    private String trangThai; // PENDING, CONFIRMED, CANCELLED
    private String lyDoHuy;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getKhachId() { return khachId; }
    public void setKhachId(Long khachId) { this.khachId = khachId; }
    public Long getXeId() { return xeId; }
    public void setXeId(Long xeId) { this.xeId = xeId; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public String getLyDoHuy() { return lyDoHuy; }
    public void setLyDoHuy(String lyDoHuy) { this.lyDoHuy = lyDoHuy; }
}