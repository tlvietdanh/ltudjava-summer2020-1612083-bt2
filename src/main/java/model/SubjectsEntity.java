package model;

import javax.persistence.*;

@Entity
@Table(name = "subjects", schema = "1612083_hibernate", catalog = "")
public class SubjectsEntity {
    private int subjectId;
    private String name;
    private String room;

    @Id
    @Column(name = "subjectID", nullable = false)
    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 256)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "room", nullable = true, length = 256)
    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubjectsEntity that = (SubjectsEntity) o;

        if (subjectId != that.subjectId) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (room != null ? !room.equals(that.room) : that.room != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = subjectId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        return result;
    }
}
