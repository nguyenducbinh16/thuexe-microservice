package com.nhom4.thuexe.xe.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "xe")
public class Xe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bienSo;
    private String hangXe;
    private boolean daKiemDinh; // true = đã kiểm định
    private boolean dangChoThue;

    // Getters và Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBienSo() { return bienSo; }
    public void setBienSo(String bienSo) { this.bienSo = bienSo; }
    public String getHangXe() { return hangXe; }
    public void setHangXe(String hangXe) { this.hangXe = hangXe; }
    public boolean isDaKiemDinh() { return daKiemDinh; }
    public void setDaKiemDinh(boolean daKiemDinh) { this.daKiemDinh = daKiemDinh; }
    public boolean isDangChoThue() { return dangChoThue; }
    public void setDangChoThue(boolean dangChoThue) { this.dangChoThue = dangChoThue; }
}