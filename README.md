 # 이모셔 챗봇

## 급식 알려주는 자비스르 구동하는데 쓰이는 안드로이드 스튜디오 프로젝트입니다.

안드로이드 웨어 설치 한다음 디버깅 하시면 됩니다. 
프로젝트 제작 환경은 안드로이드 스튜디오 3.0 입니다.
롤리팝 이상에서 구동 확인 됩니다.

## 예제 채팅 처리

package com.ernotion.ngdb.noname_bot;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Listener extends NotificationListenerService {
    static Context context;
    static Session session = new Session();
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String now_kkutu;
        SharedPreferences pref = getSharedPreferences("pref", 0);
        SharedPreferences.Editor  editor = pref.edit();
        ArrayList<String> su_UpList;
        super.onNotificationPosted(sbn);
        if (sbn.getPackageName().equals("com.kakao.talk")) {
            Notification.WearableExtender wearableExtender = new Notification.WearableExtender(sbn.getNotification());
            for (Notification.Action act : wearableExtender.getActions())
                if (act.getRemoteInputs() != null && act.getRemoteInputs().length > 0) {//String title = statusBarNotification.getNotification().extras.getString("android.title");
                    context = getApplicationContext();
                    session.session = act;
                    session.message = sbn.getNotification().extras.get("android.text").toString();
                    session.sender = sbn.getNotification().extras.getString("android.title");
                    session.room = act.title.toString().replace("답장 (", "").replace(")", "");
                    Log.i("asd", "message : " + session.message + " sender : " + session.sender + " nroom : " + session.room + " nsession : " + session);
                    if (session.message.contains("!")) {//명령어 후 입력
                        String in_str = "";
                        try {
                            in_str = session.message.split("!")[1];
                            Log.i("asd",  in_str);
                            //급식들..
                            if(in_str.equals("급식팟"))
                                send(session.message.split(" ")[0]+"님이 급식 파티를 구합니다.\n불쌍한 혼밥충을 위해 참여해주세요");
                            else if(in_str.equals("석식팟"))
                                send(session.message.split(" ")[0]+"님이 석식 파티를 구합니다.\n불쌍한 혼밥충을 위해 참여해주세요");
                            else if (in_str.contains("급식")) {
                                if(in_str.equals("급식"))
                                    new school_meal().getTodayLunch();
                                else if ((in_str.split(" ")[0].equals("오늘") && in_str.split(" ")[1].equals("급식"))||in_str.equals("오늘급식"))
                                    new school_meal().getTodayLunch();
                                else if ((in_str.split(" ")[0].equals("내일") && in_str.split(" ")[1].equals("급식"))||in_str.equals("내일급식"))
                                    new school_meal().getTomorrowLunch();
                                else if (((in_str.split(" ")[0].equals("모든")||in_str.split(" ")[0].equals("전체")) && in_str.split(" ")[1].equals("급식"))||in_str.equals("모든급식")||in_str.equals("전체급식"))
                                    new school_meal().getAllLunch();
                                else if (in_str.contains("1") || in_str.contains("2") || in_str.contains("3") || in_str.contains("4") || in_str.contains("5") || in_str.contains("6") || in_str.contains("7") || in_str.contains("8") || in_str.contains("9") || in_str.contains("0"))
                                    if(!in_str.contains("일")&&in_str.contains("월"))
                                        new school_meal().getAllLunchByMonth(Integer.parseInt(in_str.split(" ")[0].replaceAll("[^0-9]", "")));
                                    else {
                                        try {
                                            new school_meal().getTodayLunchGlobal(Integer.parseInt(in_str.split("월")[0].replaceAll("[^0-9]", "")), Integer.parseInt(in_str.split("월")[1].split("일")[0].replaceAll("[^0-9]", "")));
                                        } catch (Exception e) {
                                            try {
                                                new school_meal().getTodayLunchGlobal(Integer.parseInt(in_str.split("/")[0].replaceAll("[^0-9]", "")), Integer.parseInt(in_str.split("/")[1].split(" ")[0].replaceAll("[^0-9]", "")));
                                            } catch (Exception e2) {
                                                try {
                                                    new school_meal().getTodayLunchGlobal(Integer.parseInt(in_str.split("\\.")[0].replaceAll("[^0-9]", "")), Integer.parseInt(in_str.split("\\.")[1].split(" ")[0].replaceAll("[^0-9]", "")));
                                                } catch (Exception e3) {
                                                    new school_meal().getTodayLunchByDay(Integer.parseInt(in_str.replaceAll("[^0-9]", "")));
                                                }
                                            }
                                        }
                                    }
                                else
                                    send("명령어를 잘못 입력하셨거나 없는 명령어입니다.");
                            }
                            else if (in_str.contains("석식")) {
                                if(in_str.equals("석식"))
                                    new school_meal().getTodayDinner();
                                else if ((in_str.split(" ")[0].equals("오늘") && in_str.split(" ")[1].equals("석식"))||in_str.equals("오늘석식"))
                                    new school_meal().getTodayDinner();
                                else if ((in_str.split(" ")[0].equals("내일") && in_str.split(" ")[1].equals("석식"))||in_str.equals("내일석식"))
                                    new school_meal().getTomorrowDinner();
                                else if (((in_str.split(" ")[0].equals("모든")||in_str.split(" ")[0].equals("전체")) && in_str.split(" ")[1].equals("석식"))||in_str.equals("모든석식")||in_str.equals("전체석식"))
                                    new school_meal().getAllDinner();
                                else if (in_str.contains("1") || in_str.contains("2") || in_str.contains("3") || in_str.contains("4") || in_str.contains("5") || in_str.contains("6") || in_str.contains("7") || in_str.contains("8") || in_str.contains("9") || in_str.contains("0"))
                                    if(!in_str.contains("일")&&in_str.contains("월"))
                                        new school_meal().getAllDinnerByMonth(Integer.parseInt(in_str.split(" ")[0].replaceAll("[^0-9]", "")));
                                    else {
                                        try {
                                            new school_meal().getTodayDinnerGlobal(Integer.parseInt(in_str.split("월")[0].replaceAll("[^0-9]", "")), Integer.parseInt(in_str.split("월")[1].split("일")[0].replaceAll("[^0-9]", "")));
                                        } catch (Exception e) {
                                            try {
                                                new school_meal().getTodayDinnerGlobal(Integer.parseInt(in_str.split("/")[0].replaceAll("[^0-9]", "")), Integer.parseInt(in_str.split("/")[1].split(" ")[0].replaceAll("[^0-9]", "")));
                                            } catch (Exception e2) {
                                                try {
                                                    new school_meal().getTodayDinnerGlobal(Integer.parseInt(in_str.split("\\.")[0].replaceAll("[^0-9]", "")), Integer.parseInt(in_str.split("\\.")[1].split(" ")[0].replaceAll("[^0-9]", "")));
                                                } catch (Exception e3) {
                                                    new school_meal().getTodayDinnerByDay(Integer.parseInt(in_str.replaceAll("[^0-9]", "")));
                                                }
                                            }
                                        }
                                    }
                                else
                                    send("명령어를 잘못 입력하셨거나 없는 명령어입니다.");
                            }
                            //끝말잇기 소스
                            else if (in_str.contains("끝말잇기")){
                                try {
                                    if (in_str.equals("끝말잇기")||in_str.equals("끝말잇기 도움말")||in_str.equals("끝말잇기도움"))
                                        send("!끝말잇기 (이어지는 말)\n명령어로 진행합니다\n현재 단어 : "+pref.getString(session.room, "아이돌마스터"));
                                    else if((session.message.split(" ")[0].equals("non@meP_김도현")||session.sender.equals("non@meP_김도현"))&&in_str.split(" ")[1].equals("초기화")){
                                        editor.putString(session.room,in_str.split(" ")[2]);
                                        editor.commit();
                                        send("끝말잇기 : 초기화되었습니다.\n현재 단어 : "+pref.getString(session.room, "아이돌마스터"));
                                    }
                                    else {
                                        now_kkutu = pref.getString(session.room, "아이돌마스터");
                                        String last;
                                        char cr = now_kkutu.charAt(now_kkutu.length()-1);
                                        last = new Character(cr).toString();
                                        if(in_str.split(" ")[1].length()<=1)
                                            send("끝말잇기 : 한글자는 불가능.\n현재 단어 : "+pref.getString(session.room, "아이돌마스터"));
                                        else if(!Pattern.matches("^[가-힣]*$",in_str.split(" ")[1]))
                                            send("끝말잇기 : 한글만 가능합니다.\n현재 단어 : "+pref.getString(session.room, "아이돌마스터"));
                                        else if(in_str.split(" ")[1].startsWith(last)){
                                            editor.putString(session.room,in_str.split(" ")[1]);
                                            editor.commit();
                                            send("끝말잇기 : 입력완료.\n현재 단어 : "+pref.getString(session.room, "아이돌마스터"));
                                        }
                                        else
                                            send("끝말잇기 : 잘못 입력하셨습니다.\n현재 단어 : "+pref.getString(session.room, "아이돌마스터"));
                                    }
                                }
                                catch (Exception e) {
                                    send("끝말잇기 : 명령어가 다릅니다.\n현재 단어 : " + pref.getString(session.room, "아이돌마스터"));
                                }
                            }
                            //수업 공지
                            else if (in_str.contains("수업")){
                                su_UpList = new ArrayList<>();
                                if(in_str.contains("추가")){
                                    String order = in_str.replace("수업 추가 ","");
                                    if(!order.equals("")){
                                        editor.remove(session.room+"수업" + pref.getInt(session.room+"수업수",0));
                                        editor.putString(session.room+"수업" + pref.getInt(session.room+"수업수",0), order);
                                        editor.putInt(session.room+"수업수",pref.getInt(session.room+"수업수",0)+1);
                                        editor.commit();
                                        send("수업 공지 : 추가 완료");
                                    }
                                    else {
                                        send("수업 공지 : 제대로 입력해주십시오");
                                    }
                                }
                                else if(in_str.contains("삭제")){
                                    String index = in_str.replace("수업 삭제 ","");
                                    if(!index.equals("")){
                                        int rvsu_Up = Integer.parseInt(index);
                                        if(rvsu_Up-1>=pref.getInt(session.room+"수업수",0))
                                            send("수업 공지 : 제대로 입력해주십시오");
                                        else {
                                            for (int i = 0; i < pref.getInt(session.room + "수업수", 0); i++)
                                                su_UpList.add(pref.getString(session.room + "수업" + i, null));
                                            editor.putInt(session.room + "수업수", pref.getInt(session.room + "수업수", 0) - 1);
                                            su_UpList.remove(rvsu_Up - 1);
                                            editor.commit();
                                            for (int i = 0; i < pref.getInt(session.room + "수업수", 0); i++) {
                                                editor.remove(session.room + "수업" + i);
                                                editor.putString(session.room + "수업" + i, su_UpList.get(i));
                                            }
                                            editor.commit();
                                            send("수업 공지 : "+rvsu_Up+"번 삭제 완료");
                                        }
                                    }
                                    else {
                                        send("수업 공지 : 제대로 입력해주십시오");
                                    }
                                }
                                else if(in_str.contains("초기화")){
                                    editor.putInt(session.room+"수업수",0);
                                    editor.commit();
                                    send("수업 공지 : 초기화 완료");
                                }
                                else if(in_str.contains("내용")||in_str.contains("목록")){
                                    String put_su_up = "수업 공지 : 수업 목록\n";
                                    if (pref.getInt(session.room+"수업수",0) == 0)
                                        put_su_up += "공지가 없습니다!";
                                    for(int i = 0;i<pref.getInt(session.room+"수업수",0);i++)
                                        put_su_up += (i+1)+"번 : "+pref.getString(session.room+"수업" + i, null)+"\n";
                                    send(put_su_up);
                                }
                                else if(in_str.equals("수업")){
                                    send("수업 공지 : 수업 명령어\n수업 추가 (내용)\n수업 삭제 (번호)\n수업 내용,수업 목록");
                                }
                            }
                            //도움말
                            else if (in_str.equals("help") || in_str.equals("도움") || in_str.equals("도움말"))
                                send(   "명령어 도움말(! 뒤에 붙여 써야함)" +
                                        "급식 기능\n" +
                                        "급식,오늘급식 / 석식,오늘석식\n" +
                                        "o일 급식,석식 - 이번달 몇일 석식\n" +
                                        "o월o일 급식,석식 - 그날 석식\n" +
                                        "(o월) 모든 급식석식 - 그달 모든 석식\n" +
                                        "끝말잇기 - 이거 쳐서 도움말 보셈\n" +
                                        "수업 - 현재 구현중\n" +
                                        "개발자 - 개발자 정보\n" +
                                        "코드,오픈소스 - github 주소\n" +
                                        "이외 히든 명령어 다수..");
                                //개발자
                            else if (in_str.equals("개발자"))
                                send("갓갓 2기 부장이자 개발자이신 김도현을 입에 올리면 안된다...");
                                //코드
                            else if (in_str.equals("코드")||in_str.equals("오픈소스")||in_str.equals("깃허브")||in_str.equals("배째")||in_str.equals("배까"))
                                send("https://github.com/Blgada12/ernotion_ChatBot");
                                //자연어 처리형 명령어
                                //승준선배
                            else if (in_str.contains("승준"))
                                send("여자를 만나고 싶어하지만 아니라고 구라치는 정통 최고 인싸");
                                //권달준
                            else if (in_str.contains("달준"))
                                send("https://www.youtube.com/channel/UCYcz1AOO2ufGJ8Ti61lLmCg\n세계최강 스트리머 권!달!준!!!!");
                                //고~~동
                            else if (in_str.contains("고동")||in_str.contains("고등어"))
                                send("https://www.facebook.com/KodeungeoIsDelicious\n섹시한 고동현의 사진들");
                                //!뒤에 아무것도 없으면 무반응
                            else if (in_str.equals("")){}
                            //이외의 명령어
                            else
                                send("잘못 입력하거나 없는 명령어입니다.");
                        }catch (Exception e){
                            //속도 향상을 위한 예외처리
                            Log.e("error", String.valueOf(e));
                        }
                    }
                    //특정 문자열이 포함되면 대답
                    else {
                        //non@meP_김도현 닉네임에 반응
                        if((session.message.split(" ")[0].equals("non@meP_김도현")||session.sender.equals("non@meP_김도현"))) {
                            //닉네임이 맞을떄
                            if (session.message.contains("봇") && session.message.contains("나와"))
                                send("예 형님");
                            if (session.message.contains("봇") && session.message.contains("안녕"))
                                send("오셨습니까 주인님");
                        }
                        else
                            //닉네임이 아닐때
                            if (session.message.contains("봇")&&session.message.contains("나와"))
                                send("내 형님은 한명뿐이다!");
                        //이모션이 포함된 말을 함
                        if (session.message.contains("이모션"))
                            send("이모션 갓 솦.. 정통 동아리!");
                        //기모띠,기모찌 라는 말을 포함함
                        if (session.message.contains("기모띠") || session.message.contains("기모찌"))
                            send("앙 기모뤼~");
                        //시공,히오,히어로즈가 포함됨
                        if (session.message.contains("시공")||session.message.contains("히오")||session.message.contains("히어로즈"))
                            send("♚♚히어로즈 오브 더 스☆톰♚♚가입시$$전원 카드팩☜☜뒷면100%증정※ ♜월드오브 워크래프트♜펫 무료증정￥ 특정조건 §§디아블로3§§★공허의유산★초상화획득기회@@ 즉시이동 http://kr.battle.net/heroes/ko/ ♚♚히어로즈 오브 더 스☆톰♚♚가입시$$전원 카드팩☜☜뒷면100%증정※ ♜월드오브 워크래프트♜펫 무료증정￥ 특정조건 §§디아블로3§§★공허의유산★초상화획득기회@@ 즉시이동http://kr.battle.net/heroes/ko/\n");
                        //부장이 포함됨
                        if (session.message.contains("부장"))
                            send("히-익 오따끄-!");
                        //찬카가 포함됨
                        if (session.message.contains("찬카"))
                            send("LOOOOOOOOORDCHANKAISNOTACCEPTABLE");
                        //아이마스,젠카이노가 포함됨
                        if (session.message.contains("아이돌마스터")||session.message.contains("아이마스")||session.message.contains("젠카이노"))
                            send("IDOLM@STER!!!!");
                    }
                }
            stopSelf();
        }
    }
    public static void send(String message) {
        Intent sendIntent = new Intent();
        Bundle msg = new Bundle();
        for (RemoteInput inputable : session.session.getRemoteInputs())
            msg.putCharSequence(inputable.getResultKey(), message);
        RemoteInput.addResultsToIntent(session.session.getRemoteInputs(), sendIntent, msg);
        try {
            session.session.actionIntent.send(context, 0, sendIntent);
            Log.i("send() complete",message);
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
