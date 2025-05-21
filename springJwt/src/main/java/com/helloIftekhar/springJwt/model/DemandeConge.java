package com.helloIftekhar.springJwt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Data
@Entity
@Table(name = "demande_conge")
public class DemandeConge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_employee")
    private Long idEmployee;

    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;

    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;

    private String motif;

    @Column(nullable = false)
    private String statut = "EN_ATTENTE";

    private int duree;

    @Column(name = "remplacer_par", columnDefinition = "VARCHAR(255) DEFAULT 'CCCC'")
    private String remplacerPar = "CCCC";

    @Column(name = "motif_refus", nullable = true)
    private String motifRefus;

    @Lob
    @Column(name = "document_pdf", nullable = true) // PDF optionnel
    private byte[] documentPdf;

    @Column(name = "pdf_file_name", nullable = true)
    private String pdfFileName;

    @Column(name = "remplacant_arab")
    private String remplacantArab;

    // Constructeurs
    public DemandeConge() {
    }

    public DemandeConge(Long idEmployee, Date dateCreation, Date dateDebut, Date dateFin,
                        String motif, String statut, int duree, String remplacerPar, String motifRefus) {
        this.idEmployee = idEmployee;
        this.dateCreation = dateCreation;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
        this.statut = statut;
        this.duree = duree;
        this.remplacerPar = remplacerPar;
        this.motifRefus = motifRefus;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getRemplacerPar() {
        return remplacerPar;
    }

    public void setRemplacerPar(String remplacerPar) {
        this.remplacerPar = remplacerPar;
    }

    public byte[] getDocumentPdf() {
        return documentPdf;
    }

    public void setDocumentPdf(byte[] documentPdf) {
        this.documentPdf = documentPdf;
    }

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    private String documentPath;

    // Getter et setter
    public String getDocumentPath() { return documentPath; }
    public void setDocumentPath(String documentPath) { this.documentPath = documentPath; }


}