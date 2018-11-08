package com.zoup.android.demo.animation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private DrawBoard drawBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawBoard = findViewById(R.id.draw_board);
    }

    public void setDraw(View view) {
        drawBoard.setPaintMode(DrawBoard.DRAW_MODE);
    }

    public void setEraser(View view) {
        drawBoard.setPaintMode(DrawBoard.ERASER_MODE);
    }
}
