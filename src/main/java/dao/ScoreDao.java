package dao;

import model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScoreDao {

    private Session scoreSession;
    
    public static final String SCORE_ERROR = "Hệ thống đang có lỗi, xin vui lòng thử lai sau!";
    public static final String SCORE_ERROR_FILE = "Tập tin không hợp lệ";
    public static final String SCORE_ERROR_DATA = "Dữ liệu không hợp lệ";
    public static final String SCORE_SUCCESS = "Nhập dữ liệu thành công";
    public static final String SCORE_ERROR_EXISTS = "Dữ liệu đã tồn tại";


    public String importTranscript(String fileName, String delimeter) {
        if( fileName.length() == 0) {
            return SCORE_ERROR_FILE;
        }

        File csvFile = new File(fileName);
        if (csvFile.isFile()) {
            // create BufferedReader and read data from csv
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
                String row = bufferedReader.readLine();

                while (row != null) {
                    String data[] = row.split(delimeter);
                    if(data.length != 7) {
                        return SCORE_ERROR_FILE;
                    }

                    String mssv = data.length > 0 ? data[0] : "";
                    String classID = data.length > 1 ? data[1] : "";
                    String subjectID = data.length > 2 ? data[2] : "";
                    String gk = data.length > 3 ? data[3] : "";
                    String ck = data.length > 4 ? data[4] : "";
                    String other = data.length > 5 ? data[5] : "";
                    String total = data.length > 6 ? data[6] : "";


                    if(subjectID.length() == 0 || classID.length() == 0 || mssv.length() == 0) {
                        return SCORE_ERROR_FILE;
                    }
                    addScore(mssv, classID, subjectID, Double.parseDouble(gk), Double.parseDouble(ck), Double.parseDouble(other), Double.parseDouble(total));
                    row = bufferedReader.readLine();
                }
                System.out.println("Import thanh cong!!!");
                return SCORE_SUCCESS;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Da xay ra loi trong qua trinh doc file");
                scoreSession.close();
                return SCORE_ERROR_FILE;
            } catch (IOException e) {
                System.out.println("Da xay ra loi trong qua trinh doc file");
                e.printStackTrace();
                scoreSession.close();
                return SCORE_ERROR_FILE;
            } catch (Exception e) {
                e.printStackTrace();
                scoreSession.close();
                return SCORE_ERROR_FILE;
            }
        }
        else {
            System.out.println("File khong hop le!!");
        }

        return SCORE_ERROR;
    }

    public boolean addScore(String mssv, String classID, String subjectID, double gk, double ck, double other, double total) {
        if(subjectID.length() == 0 || classID.length() == 0 || mssv.length() == 0) {
            return false;
        }
        try {
            scoreSession = HibernateUtil.getSessionFactory().openSession();

            String hql = "FROM ClassesEntity c WHERE c.classId='"+classID+"'";
            Query query = scoreSession.createQuery(hql);
            if(query.getResultList().size() == 0) {
                System.out.println("Lop hoc khong ton tai!");
                return false;
            }
            Transaction transaction = null;

            // create subject if not exists
            ScoresEntity scoresEntity = new ScoresEntity();
            scoresEntity.setClassId(classID);
            scoresEntity.setStudentId(mssv);
            scoresEntity.setSubjectId(subjectID);
            scoresEntity.setMidTermScore(gk);
            scoresEntity.setEndTermScore(ck);
            scoresEntity.setOtherScore(other);
            scoresEntity.setTotalScore(total);


            transaction = scoreSession.beginTransaction();
            scoreSession.save(scoresEntity);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        scoreSession.close();
        return true;
    }

    public String editScore(String mssv, String classID, String subjectID, double gk, double ck, double other, double total) {
        if(classID.length() == 0 || subjectID.length() == 0 || mssv.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return SCORE_ERROR_DATA;
        }
        try{
            scoreSession = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = null;
            // score
            String getListStudent = "Select c from ScoresEntity c where c.studentId='"+mssv+"' and  c.classId='"+classID+"' and c.subjectId='" + subjectID + "' order by c.studentId";
            Query query = scoreSession.createQuery(getListStudent);
            List<ScoresEntity> score = query.getResultList();

            if(score.size() == 0) {
                ScoreDao s = new ScoreDao();
                s.addScore(mssv, classID, subjectID, gk, ck, other, total);
            }
            else {
                transaction = scoreSession.beginTransaction();
                ScoresEntity mScore = score.get(0);
                mScore.setMidTermScore(gk);
                mScore.setEndTermScore(ck);
                mScore.setOtherScore(other);
                mScore.setTotalScore(total);

                scoreSession.update(mScore);
                transaction.commit();
            }
            scoreSession.close();
            return SCORE_SUCCESS;

        } catch (Exception e) {
            e.printStackTrace();
        }
        scoreSession.close();

        return SCORE_ERROR;
    }

    public List xemdiem(String classID, String subjectID, String mssv) {
        if(classID.length() == 0 || subjectID.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return null;
        }
        try{
            scoreSession = HibernateUtil.getSessionFactory().openSession();

            // lay  danh  sach hoc sinh
            String getListStudent = "Select c, s.name from ScoresEntity c, StudentsEntity s where s.studentId = c.studentId and c.studentId='"+mssv+"' and  c.classId='"+classID+"' and c.subjectId='" + subjectID + "'";
            Query query = scoreSession.createQuery(getListStudent);
            List listScore = query.getResultList();


            scoreSession.close();
            return listScore;

        } catch (Exception e) {
            e.printStackTrace();
            scoreSession.close();
        }
        return null;
    }

    public List<String> getListClass() {
        try {
            scoreSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "select c from ClassesEntity c group by c.classId";
            Query q = scoreSession.createQuery(hql);
            List<ClassesEntity> result = q.getResultList();

            List<String> finalResult = new ArrayList<>();

            for (int i = 0; i < result.size(); i++) {
                finalResult.add(result.get(i).getClassId());
            }

            scoreSession.close();
            return finalResult;
        } catch (Exception e) {
            e.printStackTrace();
            scoreSession.close();
        }
        return null;
    }

    public List<String> getListSubject(String classId) {
        try {
            scoreSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "select m from SchedulesEntity s, SubjectsEntity m where m.subjectId = s.subjectId and s.classId='"+classId+"' group by m.subjectId";
            Query q = scoreSession.createQuery(hql);
            List<SubjectsEntity> result = q.getResultList();
            scoreSession.close();

            List<String> finalResult = new ArrayList<>();

            for (int i = 0; i < result.size(); i++) {
                finalResult.add(result.get(i).getSubjectId());
            }
            return finalResult;

        } catch (Exception e) {
            e.printStackTrace();
            scoreSession.close();
        }
        return null;
    }

    public List<String> getListClassOfStudent(String StudentID) {
        if(StudentID.length() == 0) {
            return null;
        }
        try {
            scoreSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "select c from ClassesEntity c where c.studentId='"+StudentID+"' group by c.classId";
            Query q = scoreSession.createQuery(hql);
            List<ClassesEntity> result = q.getResultList();

            String hql1 = "select s from SpecialstudentsEntity s where s.studentId='"+StudentID+"' group by s.classId";
            Query q1 = scoreSession.createQuery(hql1);
            List<SpecialstudentsEntity> result1 = q1.getResultList();

            List<String> finalResult = new ArrayList<>();

            for (int i = 0; i < result.size(); i++) {
                if(finalResult.indexOf(result.get(i).getClassId()) == -1) {
                    finalResult.add(result.get(i).getClassId());
                }
            }
            for (int i = 0; i < result1.size(); i++) {
                if(finalResult.indexOf(result1.get(i).getClassId()) == -1) {
                    finalResult.add(result1.get(i).getClassId());
                }
            }


            scoreSession.close();
            return finalResult;

        } catch (Exception e) {
            e.printStackTrace();
            scoreSession.close();
        }
        return null;
    }

    public List<String> getListSubjectOfStudent(String StudentID, String classID) {
        if(StudentID.length() == 0) {
            return null;
        }
        try {
            scoreSession = HibernateUtil.getSessionFactory().openSession();
            // get list subject;
            String hql = "select s1 from SchedulesEntity s, SubjectsEntity s1, ClassesEntity c where c.studentId='"+StudentID+"' and c.classId = s.classId and c.classId='"+classID+"' and s1.subjectId = s.subjectId group by s1.subjectId";
            Query q = scoreSession.createQuery(hql);
            List<SubjectsEntity> result = q.getResultList();
            // get list special
            String hql1 = "select s from SpecialstudentsEntity s where s.studentId='"+StudentID+"' and s.classId='"+ classID +"' group by s.subjectId";
            Query q1 = scoreSession.createQuery(hql1);
            List<SpecialstudentsEntity> result1 = q1.getResultList();

            List<String> finalResult = new ArrayList<>();

            for (int i = 0; i < result.size(); i++) {
                if(finalResult.indexOf(result.get(i).getSubjectId()) == -1) {
                    finalResult.add(result.get(i).getSubjectId());
                }
            }
            for (int i = 0; i < result1.size(); i++) {
                if(result1.get(i).getType() == -1) {
                    finalResult.remove(result1.get(i).getSubjectId());
                }
                else {
                    if (finalResult.indexOf(result1.get(i).getSubjectId()) == -1) {
                        finalResult.add(result1.get(i).getSubjectId());
                    }
                }
            }


            scoreSession.close();
            return finalResult;

        } catch (Exception e) {
            e.printStackTrace();
            scoreSession.close();
        }
        return null;
    }

    public boolean checkScore(String classID, String subjectID, String studentID) {
        if(classID.length() == 0 || subjectID.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return false;
        }
        try{
            scoreSession = HibernateUtil.getSessionFactory().openSession();

            // lay  danh  sach hoc sinh
            String getListStudent = "Select c from ScoresEntity c, StudentsEntity s where s.studentId = c.studentId and c.studentId='"+studentID+"' and  c.classId='"+classID+"' and c.subjectId='" + subjectID + "'";
            Query query = scoreSession.createQuery(getListStudent);
            List<ScoresEntity> listScore = query.getResultList();
            scoreSession.close();

            if(listScore.size() > 0) {
                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            scoreSession.close();
        }
        return false;
    }
}
