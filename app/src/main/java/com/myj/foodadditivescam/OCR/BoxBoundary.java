package com.myj.foodadditivescam.OCR;

import android.util.Log;

import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Block;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Vertex;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BoxBoundary {
    private static final String TAG = OCRMainActivity.class.getSimpleName();

    // boundary 화
    public List wordBoundary(BatchAnnotateImagesResponse response){
        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
        List boundary = new ArrayList();
        // 단어별
        for(int labelidx=1; labelidx<labels.size();labelidx++){
            List<Vertex> bound = labels.get(labelidx).getBoundingPoly().getVertices();
            if (bound != null) {
                boundary.add((float)bound.get(0).getX()); boundary.add((float)bound.get(0).getY());
                boundary.add((float)bound.get(1).getX()); boundary.add((float)bound.get(1).getY());
                boundary.add((float)bound.get(2).getX()); boundary.add((float)bound.get(2).getY());
                boundary.add((float)bound.get(3).getX()); boundary.add((float)bound.get(3).getY());

                Log.d("minjeong", "roi_width: "+bound.get(1).getX()+"   roi_hieght: "+bound.get(0).getX());
            } else {
                Log.d("minjeong", "boundary failed");
            }
        }
        return boundary;
    }
    public List ocrAllBoundary(BatchAnnotateImagesResponse response){
        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
        List boundary = new ArrayList();
        // ocr한 범위 전체
        if (labels != null) {
            List<Vertex> bound = labels.get(0).getBoundingPoly().getVertices();
            boundary.add((float)bound.get(0).getX()); boundary.add((float)bound.get(0).getY());
            boundary.add((float)bound.get(1).getX()); boundary.add((float)bound.get(1).getY());
            boundary.add((float)bound.get(2).getX()); boundary.add((float)bound.get(2).getY());
            boundary.add((float)bound.get(3).getX()); boundary.add((float)bound.get(3).getY());

            Log.d("minjeong", "roi_width: "+bound.get(1).getX()+"   roi_hieght: "+bound.get(0).getX());
        } else {
            Log.d("minjeong", "boundary failed");
        }
        return boundary;
    }
    public List ParagraphBoundary(BatchAnnotateImagesResponse response){
        //Paragraph별
        List boundary = new ArrayList();
        try {
            List<Block> labels = response.getResponses().get(0).getFullTextAnnotation().getPages().get(0).getBlocks();
            for (int labelidx = 0; labelidx < labels.size(); labelidx++) {
                Log.d("minjeong", "paragh size: " + labels.size());
                List<Vertex> bound = labels.get(labelidx).getParagraphs().get(0).getBoundingBox().getVertices();
                if (labels.get(labelidx) != null) {
                    boundary.add((float) bound.get(0).getX());
                    boundary.add((float) bound.get(0).getY());
                    boundary.add((float) bound.get(1).getX());
                    boundary.add((float) bound.get(1).getY());
                    boundary.add((float) bound.get(2).getX());
                    boundary.add((float) bound.get(2).getY());
                    boundary.add((float) bound.get(3).getX());
                    boundary.add((float) bound.get(3).getY());

                    Log.d("minjeong", "roi_width: " + bound.get(1).getX() + "   roi_hieght: " + bound.get(0).getX());
                } else {
                    Log.d("minjeong", "boundary failed");
                }
            }
        }catch (Exception e){
            Log.d(TAG, "paragraphBoundary error: "+e);
        }

        return boundary;
    }
}