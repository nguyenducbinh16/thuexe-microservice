package com.nhom4.thuexe.khach.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "khach")
public class Khach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hoTen;
    private BigDecimal soDuVi;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public BigDecimal getSoDuVi() { return soDuVi; }
    public void setSoDuVi(BigDecimal soDuVi) { this.soDuVi = soDuVi; }
}