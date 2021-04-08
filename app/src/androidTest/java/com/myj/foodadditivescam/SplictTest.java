package com.myj.foodadditivescam;

import java.util.ArrayList;
import java.util.List;

class SplitTest {
    public static void main(String args[]) {
        String text = "·수입판매원: 롯데제과(주)서울시 영등포구양평로21길25·제조원: The " +
                "Hershey Company·유통기한: 측면표기일까지(연.월.일) 원재료명: D-소비" +
                "톨 87175%,DL-사과산,말토덱스트린,D-말티톨 1.8723%,구연산,아스파탐(감미로," +
                "페닐알라닌 함유), 합성향로,스테이린산마그네슘, 합성향료로, L-주석산,유화제1, 유화제Ⅱ," +
                "아라비아검, 엘더베리쥬스분말, B-카로틴(착색료) 대두함유·내포장재질: 폴리프로필렌-직사광" +
                "선및습기를 피해 진열·보관·습기로인해변색될 수 있으나인체에 무해함·유통 중변질품은 구입상점" +
                "및본사에서 항상교환·소비자기본법에 의한 피해보상·부정, 불량식품 신고는 국번없이1399-과랑" +
                "섭취시설사를 일으킬 수 있음-신맛이 강해 입안에 자극을 유발할 수있음";

        String text2 ="·제품명 : 오틀리 오트 드링크 초콜릿맛\r\n" +
                "(OATLY OAT DRINK CHOCOLATE) (귀리 10%, 코코아분말 1%)\r\n" +
                "· 식품유형 : 혼합음료 ·제조업소 : OATLY AB . 내용량 : 250ml(141 kcal|\r\n" +
                "•수입판매업소 : (주동서 / 서울특별시 마포구 독막로 324\r\n" +
                "(소비자 상담실 : 080-003-2002)\r\n" +
                "· 유통기한 : 제품 윗면 표시일까지(일.월.년 순)\r\n" +
                "원재료명 : 정제수, 귀리 10%, 설탕, 코코아분말 1%, 유채유, 탄산갈용,\r\n" +
                "· 원산지 : 스웨덴\r\n" +
                "제삼인산칼슘, 정제소금, 합성향료, 비타민(B2, D2, B12)\r\n" +
                "'내면포장재질 : 내포장폴리에틸렌, 빨대-폴리프로필렌\r\n" +
                "'보관방법 : 직사광선을 피한 건냉한 곳에 보관(0~25°C).\r\n" +
                "개봉 후 냉장보관하시고 5일 이내에 드시기 바랍니다.\r\n" +
                "' 충분히 흔든 후 드시기 바랍니다.\r\n" +
                "'반품 및 교환처 : 구입처 또는 (주동서\r\n" +
                "• 부정·불량식품 신고는 국번없이 1399\r\n" +
                "• 이 제품은 공정거래위원회고시 소비자분쟁해결기준에\r\n" +
                "의거 교환 또는 보상을 받을 수 있습니다.\r\n" +
                "종이팩\r\n" +
                "빨대 : PP\r\n" +
                "영양정보\r\n" +
                "|나트륨 150mg 8% | 탄수화물 25g 8%\r\n" +
                "|식이섬유 2.7g 11% | 지방 3.8g 7%\r\n" +
                "포화지방 0.5g 3% | 콜레스테롤 Omg 0%| 단백질 3g 5%\r\n" +
                "칼슘 300mg 43%\r\n" +
                "비타민B12 0.95 xg 40%\r\n" +
                "일성문 기준처에 대한 테율은 2000 cal 7준이드로 개인의 필요 에 피리 대를 수 있습니다.\r\n" +
                "총 내용량 250ml 141 kcal\r\n" +
                "당류 19g 19%\r\n" +
                "트랜스지방 Og\r\n" +
                "비타민D 1.25 g 13%\r\n" +
                "비타민B2 0.52mg 37%";

        //읽어오는 과정에서 생긴 엔터 삭제
        text2.replace("\r\n", "");

        //읽어온 문자열 쉼표로 분리
        //txtLst: 쉼표로 분리한 배열
        //txtLst2: 쉼표와 띄어쓰기로 분리한 리스트
        //strlst: txtLst의 원소 하나하나를 띄어쓰기로 분리한 배열
        String[] txtLst=text2.split(",");
        List<String> txtLst2 = new ArrayList<String>();
        for(int i = 0; i<txtLst.length; i++) {
            String[] strlst = txtLst[i].split(" ");
            for(int j = 0; j<strlst.length;j++) {
                txtLst2.add(strlst[j]);
            }
        }

        System.out.println(txtLst2);

        int index=0;
        //원재료명이라는 텍스트를 찾아서
        //그 이전의 데이터 삭제
        for(int i=0; i<txtLst2.size(); i++) {
            if(txtLst2.get(i).contains("원재료명")) {
                index = i;
                break;
            }
        }
        for(int i=0; i<index; i++) {
            txtLst2.remove(0);
        }

        System.out.println("\n\n\n\n\n---------------------\n");
        System.out.println(txtLst2);


    }
}
