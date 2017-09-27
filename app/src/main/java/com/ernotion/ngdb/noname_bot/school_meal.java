package com.ernotion.ngdb.noname_bot;

import org.hyunjun.school.School;
import org.hyunjun.school.SchoolException;
import org.hyunjun.school.SchoolMenu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//급식 파싱해오는 클래스
//school api라는 공개 api사용
public class school_meal {
    //학교코드로 선린 불러옴
    final String SUNRIN_HIGHSCHOLL_CODE = "B100000658";
    School api = new School(School.Type.HIGH, School.Region.SEOUL, SUNRIN_HIGHSCHOLL_CODE);
    //호출된 순간의 날짜 불러옴
    class getDate{
        int DD;
        int MM;
        int YYYY;
        getDate(){
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            YYYY = Integer.parseInt(new SimpleDateFormat("y").format(date));
            MM = Integer.parseInt(new SimpleDateFormat("M").format(date));
            DD = Integer.parseInt(new SimpleDateFormat("d").format(date));
        }
    }
    //오늘 급식 불러오기
    public void getTodayLunch() {
        getDate today = new getDate();
        getLunch(today.YYYY,today.MM,today.DD);
    }
    //내일 급식 불러오기
    public void getTomorrowLunch() {
        getDate today = new getDate();
        getLunch(today.YYYY,today.MM,today.DD+1);
    }
    //오늘 석식 불러오기
    public void getTodayDinner() {
        getDate today = new getDate();
        getDinner(today.YYYY,today.MM,today.DD);
    }
    //일자만 받아와 그 날짜의 급식 불러오기
    public void getTodayLunchByDay(int DD) {
        getDate today = new getDate();
        getLunch(today.YYYY,today.MM,DD);
    }
    //월일 다 받아와 입력받은 날짜의 급식 불러오기
    public void getTodayLunchGlobal(int MM,int DD) {
        getDate today = new getDate();
        getLunch(today.YYYY,MM,DD);
    }
    //일자만 받아와 그 날짜의 석식 불러오기
    public void getTodayDinnerByDay(int DD) {
        getDate today = new getDate();
        getDinner(today.YYYY,today.MM,DD);
    }
    //월일 다 받아와 입력받은 날짜의 석식 불러오기
    public void getTodayDinnerGlobal(int MM,int DD) {
        getDate today = new getDate();
        getDinner(today.YYYY,MM,DD);
    }
    //내일 석식 불러오기
    public void getTomorrowDinner() {
        getDate today = new getDate();
        getDinner(today.YYYY,today.MM,today.DD+1);
    }
    //점심 인터넷에서 받아오는 본체
    public void getLunch(final int YYYY, final int MM, final int DD){
        //인터넷 연결은 쓰레드 안에서
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<SchoolMenu> menu = api.getMonthlyMenu(YYYY, MM);
                    Listener.send(YYYY+"-"+MM+"-"+DD+"\n"+menu.get(DD-1).lunch.replace("&amp;",","));
                } catch (SchoolException e) {
                    Listener.send("ERROR");
                    e.printStackTrace();
                }
            }
        }).start();


    }
    //석식 인터넷에서 받아오는 본체
    public void getDinner(final int YYYY, final int MM, final int DD){
        //인터넷 연결은 쓰레드 안에서
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    List<SchoolMenu> menu = api.getMonthlyMenu(YYYY, MM);
                    Listener.send(YYYY+"-"+MM+"-"+DD+"\n"+menu.get(DD-1).dinner.replace("&amp;","&"));
                } catch (SchoolException e) {
                    Listener.send("ERROR");
                    e.printStackTrace();
                }
            }
        }).start();

    }
    //이번 달의 모든 점심 불러오기
    public void getAllLunch(){
        final getDate today = new getDate();
        //인터넷 연결은 쓰레드 안에서
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<SchoolMenu> menu = api.getMonthlyMenu(today.YYYY,today.MM);
                    String message = "전체 급식\n";
                    for (int i=0;i<menu.size();i++)
                        message+=today.YYYY+"-"+today.MM+"-"+(i+1)+"\n"+menu.get(i).lunch.replace("&amp;","&")+"\n\n";
                    Listener.send(message);
                } catch (SchoolException e) {
                    Listener.send("ERROR");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //입력받은 달의 모든 점심 불러오기
    public void getAllLunchByMonth(final int MM){
        final getDate today = new getDate();
        //인터넷 연결은 쓰레드 안에서
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<SchoolMenu> menu = api.getMonthlyMenu(today.YYYY,MM);
                    String message = "전체 급식\n";
                    for (int i=0;i<menu.size();i++)
                        message+=today.YYYY+"-"+MM+"-"+(i+1)+"\n"+menu.get(i).lunch.replace("&amp;","&")+"\n\n";
                    Listener.send(message);
                } catch (SchoolException e) {
                    Listener.send("ERROR");
                    e.printStackTrace();
                }
            }
        }).start();

    }
    //이번 달의 모든 석식 불러오기
    public void getAllDinner(){
        final getDate today = new getDate();
        //인터넷 연결은 쓰레드 안에서
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<SchoolMenu> menu = api.getMonthlyMenu(today.YYYY,today.MM);
                    String message = today.MM+"월 전체 석식\n";
                    for (int i=0;i<menu.size();i++)
                        message+=today.YYYY+"-"+today.MM+"-"+(i+1)+"\n"+menu.get(i).dinner.replace("&amp;","&")+"\n\n";
                    Listener.send(message);
                } catch (SchoolException e) {
                    Listener.send("ERROR");
                    e.printStackTrace();
                }
            }
        }).start();

    }
    //입력받은 달의 모든 석식 불러오기
    public void getAllDinnerByMonth(final int MM){
        final getDate today = new getDate();
        //인터넷 연결은 쓰레드 안에서
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<SchoolMenu> menu = api.getMonthlyMenu(today.YYYY,MM);
                    String message = MM+"월 전체석식\n";
                    for (int i=0;i<menu.size();i++)
                        message+=today.YYYY+"-"+MM+"-"+(i+1)+"\n"+menu.get(i).dinner.replace("&amp;","&")+"\n\n";
                    Listener.send(message);
                } catch (SchoolException e) {
                    Listener.send("ERROR");
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
