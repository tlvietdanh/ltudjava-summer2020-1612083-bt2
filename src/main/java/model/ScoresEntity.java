package model;

import javax.persistence.*;

@Entity
@Table(name = "scores", schema = "1612083_hibernate", catalog = "")
@IdClass(ScoresEntityPK.class)
public class ScoresEntity {
    private String subjectId;
    private String studentId;
    private String classId;
    private Double midTermScore;
    private Double endTermScore;
    private Double otherScore;
    private Double totalScore;

    @Id
    @Column(name = "subjectID")
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    @Id
    @Column(name = "studentID")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Id
    @Column(name = "classID")
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Basic
    @Column(name = "midTermScore")
    public Double getMidTermScore() {
        return midTermScore;
    }

    public void setMidTermScore(Double midTermScore) {
        this.midTermScore = midTermScore;
    }

    @Basic
    @Column(name = "endTermScore")
    public Double getEndTermScore() {
        return endTermScore;
    }

    public void setEndTermScore(Double endTermScore) {
        this.endTermScore = endTermScore;
    }

    @Basic
    @Column(name = "otherScore")
    public Double getOtherScore() {
        return otherScore;
    }

    public void setOtherScore(Double otherScore) {
        this.otherScore = otherScore;
    }

    @Basic
    @Column(name = "totalScore")
    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoresEntity that = (ScoresEntity) o;

        if (subjectId != null ? !subjectId.equals(that.subjectId) : that.subjectId != null) return false;
        if (studentId != null ? !studentId.equals(that.studentId) : that.studentId != null) return false;
        if (classId != null ? !classId.equals(that.classId) : that.classId != null) return false;
        if (midTermScore != null ? !midTermScore.equals(that.midTermScore) : that.midTermScore != null) return false;
        if (endTermScore != null ? !endTermScore.equals(that.endTermScore) : that.endTermScore != null) return false;
        if (otherScore != null ? !otherScore.equals(that.otherScore) : that.otherScore != null) return false;
        if (totalScore != null ? !totalScore.equals(that.totalScore) : that.totalScore != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subjectId != null ? subjectId.hashCode() : 0;
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        result = 31 * result + (classId != null ? classId.hashCode() : 0);
        result = 31 * result + (midTermScore != null ? midTermScore.hashCode() : 0);
        result = 31 * result + (endTermScore != null ? endTermScore.hashCode() : 0);
        result = 31 * result + (otherScore != null ? otherScore.hashCode() : 0);
        result = 31 * result + (totalScore != null ? totalScore.hashCode() : 0);
        return result;
    }
}
