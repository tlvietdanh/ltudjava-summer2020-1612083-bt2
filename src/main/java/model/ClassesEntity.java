package model;

import javax.persistence.*;

@Entity
@Table(name = "classes", schema = "1612083_hibernate", catalog = "")
@IdClass(ClassesEntityPK.class)
public class ClassesEntity {
    private String classId;
    private String studentId;

    @Id
    @Column(name = "classID")
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Id
    @Column(name = "studentID")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassesEntity that = (ClassesEntity) o;

        if (classId != null ? !classId.equals(that.classId) : that.classId != null) return false;
        if (studentId != null ? !studentId.equals(that.studentId) : that.studentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = classId != null ? classId.hashCode() : 0;
        result = 31 * result + (studentId != null ? studentId.hashCode() : 0);
        return result;
    }
}
