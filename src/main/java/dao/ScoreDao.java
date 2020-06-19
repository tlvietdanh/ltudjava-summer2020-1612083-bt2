package dao;

import model.SchedulesEntity;
import model.ScoresEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtil;

import java.io.*;
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
                return SCORE_ERROR_FILE;
            } catch (IOException e) {
                System.out.println("Da xay ra loi trong qua trinh doc file");
                e.printStackTrace();
                return SCORE_ERROR_FILE;
            } catch (Exception e) {
                e.printStackTrace();
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

    public boolean editScore(String mssv, String classID, String subjectID, double gk, double ck, double other, double total) {
        if(classID.length() == 0 || subjectID.length() == 0 || mssv.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return false;
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
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        scoreSession.close();

        return false;
    }

    public boolean xemdiem(String classID, String subjectID, String mssv) {
        if(classID.length() == 0 || subjectID.length() == 0) {
            System.out.println("Ma loi ko hop le");
            return false;
        }
        scoreSession = HibernateUtil.getSessionFactory().openSession();
        try{

            // lay  danh  sach hoc sinh
            String getListStudent = "Select c from ScoresEntity c where c.studentId='"+mssv+"' and  c.classId='"+classID+"' and c.subjectId='" + subjectID + "'";
            Query query = scoreSession.createQuery(getListStudent);
            List<ScoresEntity> listScore = query.getResultList();

            if(listScore.size() > 0) {
                ScoresEntity score = listScore.get(0);
                System.out.println(score.getStudentId() + "  " + "  " + score.getMidTermScore() + "  " + score.getEndTermScore() + "  " + score.getOtherScore() + "  " + score.getTotalScore());
            }
            else {
                System.out.println("Chua co diem");
            }

            scoreSession.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        scoreSession.close();

        return false;
    }

    public List<ScoresEntity> getListClass() {
        try {
            scoreSession = HibernateUtil.getSessionFactory().openSession();
            String hql = "select c from ScoresEntity c group by c.classId, c.subjectId";
            Query q = scoreSession.createQuery(hql);
            List<ScoresEntity> result = q.getResultList();
            scoreSession.close();
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
