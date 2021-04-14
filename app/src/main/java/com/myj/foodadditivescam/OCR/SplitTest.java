package com.myj.foodadditivescam.OCR;

import java.util.ArrayList;
import java.util.List;

class SplitTest {

    public static List<String> splitText(String text) {
        //읽어오는 과정에서 생긴 엔터 삭제
        text = text.replace("\r\n", " ");
        text = text.replace("\n", " ");

//        //괄호 안 데이터 삭제
//		while(true) {
//			if(text.contains("(")){
//				int index1 = text.indexOf('(');
//				int index2 = text.indexOf(')');
//
//			}else {
//				break;
//			}
//
//		}

        //읽어온 문자열 쉼표로 분리
        //txtLst: 쉼표로 분리한 배열
        //txtLst2: 쉼표와 띄어쓰기로 분리한 리스트
        //strlst: txtLst의 원소 하나하나를 띄어쓰기로 분리한 배열
        String[] txtLst=text.split(",");
        List<String> txtLst2 = new ArrayList<String>();
        for(int i = 0; i<txtLst.length; i++) {
            String[] strlst = txtLst[i].split(" ");
            for(int j = 0; j<strlst.length;j++) {
                txtLst2.add(strlst[j]);
            }
        }

        int index=0;
        //원재료명이라는 텍스트를 찾아서
        for(int i=0; i<txtLst2.size(); i++) {
            if(txtLst2.get(i).contains("원재료")) {
                index = i;
                break;
            }
        }
        //그 이전의 데이터 삭제
        for(int i=0; i<index; i++) {
            txtLst2.remove(0);
        }

        for(int i = 0; i<txtLst2.size();i++) {
            //비어있는 데이터 삭제
            if(txtLst2.get(i).length()==0) {
                txtLst2.remove(i);
            }

            //의미없는 띄어쓰기 데이터, 숫자(퍼센트) 삭제
            char txt = txtLst2.get(i).charAt(0);
            if(txt==' ' ||txt=='1'|| txt=='2'|| txt=='3'|| txt=='4'
                    || txt=='5'|| txt=='6'|| txt=='7'|| txt=='8'|| txt=='9'|| txt=='0') {

                txtLst2.remove(i);
                i--;
             //특수문자 삭제
            }else if(txt==':'|| txt=='·'|| txt=='•'|| txt=='|'|| txt=='\'') {
                if (txtLst2.get(i).length() == 1) {
                    txtLst2.remove(i);
                    i--;
                }
            }
        }
        return txtLst2;
    }
}
