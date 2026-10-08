package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button rollButton;
    private TextView resultText;
    private Spinner diceSpinner;
    private GridLayout diceGrid;

    private Random random = new Random();

    // Stores all currently displayed dice
    private List<DiceView> diceViews = new ArrayList<>();

    // Number of dice currently selected
    private int numberOfDice = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        rollButton = findViewById(R.id.rollButton);
        resultText = findViewById(R.id.resultText);
        diceSpinner = findViewById(R.id.diceSpinner);
        diceGrid = findViewById(R.id.diceGrid);


        // Dropdown options
        String[] diceOptions = {
                "1",
                "2",
                "3",
                "4"
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                diceOptions
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        diceSpinner.setAdapter(adapter);


        // When dropdown selection changes
        diceSpinner.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        numberOfDice = position + 1;

                        createDice();
                    }


                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {

                    }
                }
        );


        // Roll button
        rollButton.setOnClickListener(v -> rollDice());


        // Create the first die
        createDice();
    }

    private void createDice() {

        // Remove old dice
        diceGrid.removeAllViews();

        // Clear the list
        diceViews.clear();

        for (int i = 0; i < numberOfDice; i++) {

            DiceView diceView = new DiceView(this);

            // 90dp size
            int size = (int) (
                    90 * getResources().getDisplayMetrics().density
            );

            GridLayout.LayoutParams params =
                    new GridLayout.LayoutParams();

            params.width = size;
            params.height = size;

            params.setMargins(10, 10, 10, 10);

            /*
             * If there are 3 dice and this is the
             * third die, make it span both columns.
             */
            if (numberOfDice == 3 && i == 2) {

                params.columnSpec =
                        GridLayout.spec(
                                0,
                                2,
                                GridLayout.CENTER
                        );
            }

            diceView.setLayoutParams(params);

            // Start with number 1
            diceView.setNumber(1);

            // Add dice to grid
            diceGrid.addView(diceView);

            // Save dice
            diceViews.add(diceView);
        }

        // Reset result
        resultText.setText("Result: -");
    }

    /**
     * Rolls all selected dice.
     */
    private void rollDice() {

        rollButton.setEnabled(false);

        resultText.setText("Rolling...");


        // Store results for every die
        List<Integer> results = new ArrayList<>();


        // Generate random result for every die
        for (int i = 0; i < numberOfDice; i++) {

            int result = random.nextInt(6) + 1;

            results.add(result);
        }


        // Animate every die
        List<Animator> animations = new ArrayList<>();


        for (DiceView diceView : diceViews) {

            ObjectAnimator rotationX =
                    ObjectAnimator.ofFloat(
                            diceView,
                            "rotationX",
                            0f,
                            360f,
                            720f,
                            1080f
                    );


            ObjectAnimator rotationY =
                    ObjectAnimator.ofFloat(
                            diceView,
                            "rotationY",
                            0f,
                            360f,
                            720f,
                            1080f
                    );


            AnimatorSet diceAnimation = new AnimatorSet();

            diceAnimation.playTogether(
                    rotationX,
                    rotationY
            );

            diceAnimation.setDuration(1200);

            diceAnimation.setInterpolator(
                    new DecelerateInterpolator()
            );


            animations.add(diceAnimation);
        }


        // Combine all dice animations
        AnimatorSet allAnimations = new AnimatorSet();

        allAnimations.playTogether(animations);


        allAnimations.addListener(
                new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(
                            Animator animation) {

                        resultText.setText("Rolling...");
                    }


                    @Override
                    public void onAnimationEnd(
                            Animator animation) {

                        StringBuilder resultString =
                                new StringBuilder();

                        // Set each die's result
                        for (int i = 0; i < diceViews.size(); i++) {

                            DiceView diceView =
                                    diceViews.get(i);

                            int result =
                                    results.get(i);

                            diceView.setNumber(result);


                            if (i > 0) {
                                resultString.append(", ");
                            }

                            resultString.append(result);
                        }


                        // Display result
                        resultText.setText(
                                "You rolled: " +
                                        resultString.toString()
                        );


                        // Result animation
                        resultText.setScaleX(0.7f);
                        resultText.setScaleY(0.7f);
                        resultText.setAlpha(0f);


                        resultText.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(1f)
                                .setDuration(300)
                                .start();


                        rollButton.setEnabled(true);
                    }


                    @Override
                    public void onAnimationCancel(
                            Animator animation) {

                        rollButton.setEnabled(true);
                    }


                    @Override
                    public void onAnimationRepeat(
                            Animator animation) {

                    }
                }
        );


        allAnimations.start();
    }
}