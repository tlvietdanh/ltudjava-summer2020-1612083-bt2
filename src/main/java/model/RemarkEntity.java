package model;

import javax.persistence.*;

@Entity
@Table(name = "remark", schema = "1612083_hibernate", catalog = "")
@IdClass(RemarkEntityPK.class)
public class RemarkEntity {
    private Integer remarkEventId;
    private String subjectId;
    private String studentId;
    private String classId;
    private String reason;
    private Integer type;
    private Double newScore;
    private Integer status;

    @Basic
    @Column(name = "remarkEventID")
    public Integer getRemarkEventId() {
        return remarkEventId;
    }

    public void setRemarkEventId(Integer remarkEventId) {
        this.remarkEventId = remarkEventId;
    }

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
    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Basic
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Basic
    @Column(name = "newScore")
    public Double getNewScore() {
        return newScore;
    }

    public void setNewScore(Double newScore) {
        this.newScore = newScore;
    }

    @Basic
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RemarkEntity that = (RemarkEntity) o;

        if (remarkEventId != null ? !remarkEventId.equals(that.remarkEventId) : that.remarkEventId != null)
            return false;
        if (subjectId != null ? !subjectId.equals(that.subjectId) : that.subjectId != null) return false;
        if (studentId != null ? !studentId.equals(that.studentId) : that.studentId != null) return false;
        if (classId != null ? !classId.equals(that.classId) : that.classId != null) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (newScore != null ? !newScore.equals(that.newScore) : that.newScore != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = remarkEventId != null ? remarkEventId.hashCode() : 0;
        result = 31 * result + (subjectId != null ? subjectId.hashCode() : 0);
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        result = 31 * result + (classId != null ? classId.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (newScore != null ? newScore.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
