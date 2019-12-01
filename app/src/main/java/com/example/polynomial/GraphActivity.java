package com.example.polynomial;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.softmoore.android.graphlib.Graph;
import com.softmoore.android.graphlib.GraphView;

import org.apache.commons.io.IOUtils;
import org.apache.commons.math3.analysis.solvers.LaguerreSolver;
import org.apache.commons.math3.complex.Complex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GraphActivity extends AppCompatActivity {

    @BindView(R.id.textView4)
    private TextView chartTitle;

    @BindView(R.id.graph_view)
    private GraphView graphBlock;

    private final List<Double> roots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);
        ButterKnife.bind(this);

        final int defaultFactorValue = 0;
        final int a = getIntent().getIntExtra(
                getResources().getString(R.string.intent_a_factor_param), defaultFactorValue
        );
        final int b = getIntent().getIntExtra(
                getResources().getString(R.string.intent_b_factor_param), defaultFactorValue
        );
        final int c = getIntent().getIntExtra(
                getResources().getString(R.string.intent_c_factor_param), defaultFactorValue
        );
        final String textViewMessage =
                String.format(Locale.ENGLISH,"A = %d, B = %d, C = %d", a, b, c);

        chartTitle.setText(textViewMessage);

        drawGraph(a, b, c);

        setTitle(textViewMessage);

        roots.addAll(calculateRoots(a, b, c));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(getResources().getString(R.string.intent_roots_param), roots.toString());
        setResult(2, intent);
        super.onBackPressed();
    }

    @OnClick(R.id.button2)
    protected void downloadBitmap() {
        final Bitmap bitmap = Bitmap.createBitmap(
                graphBlock.getWidth(), graphBlock.getHeight(), Bitmap.Config.ARGB_8888
        );

        drawCanvas(bitmap);

        File file = this.getApplicationContext().getExternalFilesDir(null);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(file, "graph.jpeg"));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
        } catch (FileNotFoundException e) {
            Log.d("GRAPH", "Picture saving failed");
        } finally {
            if (fos != null) {
                IOUtils.closeQuietly(fos);
            }
        }
    }

    private void drawGraph(double a, double b, double c) {
        Graph graph = new Graph.Builder()
                .addFunction(x -> (a * x * x) + (b * x) + c, Color.RED)
                .setWorldCoordinates(-20, 20, -20, 20)
                .setXTicks(new double[]{-15, -10, -5, 5, 10, 15})
                .setYTicks(new double[]{-15, -10, -5, 5, 10, 15})
                .build();
        graphBlock.setGraph(graph);
    }


    private void drawCanvas(final Bitmap bitmap) {
        final Canvas cvs = new Canvas(bitmap);
        Drawable background = graphBlock.getBackground();

        if (background == null) {
            cvs.drawColor(Color.WHITE);
        } else {
            background.draw(cvs);
        }

        graphBlock.draw(cvs);
    }

    private List<Double> calculateRoots(final double a, final double b, final double c) {
        final LaguerreSolver solver = new LaguerreSolver();
        final Complex[] roots = solver.solveAllComplex(
                new double[] {c, b, a}, -20
        );
        return Arrays.stream(roots)
                .map(Complex::getReal)
                .collect(Collectors.toList());
    }

}
