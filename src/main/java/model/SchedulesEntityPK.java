package model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class SchedulesEntityPK implements Serializable {
    private int scheduleId;
    private String subjectId;

    @Column(name = "scheduleID")
    @Id
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Column(name = "subjectID")
    @Id
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SchedulesEntityPK that = (SchedulesEntityPK) o;

        if (scheduleId != that.scheduleId) return false;
        if (subjectId != null ? !subjectId.equals(that.subjectId) : that.subjectId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = scheduleId;
        result = 31 * result + (subjectId != null ? subjectId.hashCode() : 0);
        return result;
    }
}
