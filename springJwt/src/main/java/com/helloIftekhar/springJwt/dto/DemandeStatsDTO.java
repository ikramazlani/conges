package com.helloIftekhar.springJwt.dto;

public class DemandeStatsDTO {
    private long approuvees;
    private long refusees;
    private long enAttente;
    private long total;

    // Constructeurs
    public DemandeStatsDTO() {}

    public DemandeStatsDTO(long approuvees, long refusees, long enAttente) {
        this.approuvees = approuvees;
        this.refusees = refusees;
        this.enAttente = enAttente;
        this.total = approuvees + refusees + enAttente;
    }

    // Getters et Setters
    public long getApprouvees() {
        return approuvees;
    }

    public void setApprouvees(long approuvees) {
        this.approuvees = approuvees;
    }

    public long getRefusees() {
        return refusees;
    }

    public void setRefusees(long refusees) {
        this.refusees = refusees;
    }

    public long getEnAttente() {
        return enAttente;
    }

    public void setEnAttente(long enAttente) {
        this.enAttente = enAttente;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}