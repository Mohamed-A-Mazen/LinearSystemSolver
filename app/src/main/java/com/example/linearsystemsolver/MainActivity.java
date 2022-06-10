package com.example.linearsystemsolver;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int rowIndex;
    private int columnIndex;
    private TableLayout matrixTable;
    private TableLayout constantsTable;
    private TextView solutionTextView;
    private Button delete;
    private Button rightArrow;
    private Button leftArrow;
    private Button solveButton;
    private boolean isDisplayingError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        solutionTextView = findViewById(R.id.solutionTextView);
        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);
        Button plusButton = findViewById(R.id.plusButton);
        Button minusButton = findViewById(R.id.minusButton);
        Button decimal = findViewById(R.id.decimal);
        Button jButton = findViewById(R.id.jButton);
        delete = findViewById(R.id.delete);
        rightArrow = findViewById(R.id.rightArrow);
        leftArrow = findViewById(R.id.leftArrow);
        solveButton = findViewById(R.id.solveButton);
        matrixTable = findViewById(R.id.matrixTable);
        constantsTable = findViewById(R.id.constantsTable);

        // modifying the EditTexts in the two tables so that they can't be modified via device's keyboard
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 0) {
                    // if j=0 we set up the cell with corresponding indexes in constantTable
                    EditText constantsEditText = (EditText) ((TableRow) constantsTable.getChildAt(i)).getChildAt(j);
                    constantsEditText.setShowSoftInputOnFocus(false);
                    constantsEditText.setOnFocusChangeListener(onFocusChangeListener);
                }
                EditText matrixEditText = (EditText) ((TableRow) matrixTable.getChildAt(i)).getChildAt(j);
                matrixEditText.setShowSoftInputOnFocus(false);
                matrixEditText.setOnFocusChangeListener(onFocusChangeListener);
            }
        }

        button0.setOnClickListener(onClickListener);
        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
        button4.setOnClickListener(onClickListener);
        button5.setOnClickListener(onClickListener);
        button6.setOnClickListener(onClickListener);
        button7.setOnClickListener(onClickListener);
        button8.setOnClickListener(onClickListener);
        button9.setOnClickListener(onClickListener);
        rightArrow.setOnClickListener(onClickListener);
        leftArrow.setOnClickListener(onClickListener);
        decimal.setOnClickListener(onClickListener);
        delete.setOnClickListener(onClickListener);
        jButton.setOnClickListener(onClickListener);
        plusButton.setOnClickListener(onClickListener);
        minusButton.setOnClickListener(onClickListener);
        solveButton.setOnClickListener(onClickListener);

    }

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (isDisplayingError) {
                solutionTextView.setText("");
                isDisplayingError = false;
            }
            if (b) {
                TableRow tableRow = (TableRow) view.getParent();
                if (((TableLayout) tableRow.getParent()).getId() == constantsTable.getId()) {
                    rowIndex = constantsTable.indexOfChild(tableRow);
                    columnIndex = tableRow.indexOfChild(view) + 3;
                } else {
                    rowIndex = matrixTable.indexOfChild(tableRow);
                    columnIndex = tableRow.indexOfChild(view);
                }
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText editText = getSelectedCell();
            int selectionIndex = editText.getSelectionEnd();
            if (view.getId() == solveButton.getId()) {
                solveSystemOfEquation();
            } else if (view.getId() == rightArrow.getId()) {
                // the reason this if is nested is because if it's not and the user clicks right/left arrow or delete
                // the code within the else at the end will be executed causing it to append a right arrow character to the editText
                if (selectionIndex < editText.getText().length()) {
                    editText.setSelection(selectionIndex + 1);
                }
            } else if (view.getId() == leftArrow.getId()) {
                if (selectionIndex > 0) {
                    editText.setSelection(selectionIndex - 1);
                }
            } else if (view.getId() == delete.getId()) {
                if (selectionIndex > 0) {
                    String s = editText.getText().toString();
                    s = s.substring(0, selectionIndex - 1) + s.substring(selectionIndex);
                    editText.setText(s);
                    editText.setSelection(selectionIndex - 1);
                }
            } else {
                String s = editText.getText().toString();
                StringBuilder sb = new StringBuilder(s);
                sb.insert(selectionIndex,((Button)view).getText());
                editText.setText(sb.toString());
                editText.setSelection(++selectionIndex);
            }
        }
    };

    private EditText getSelectedCell() {
        if (columnIndex == 3) {
            return (EditText) ((TableRow) constantsTable.getChildAt(rowIndex)).getChildAt(0);
        }
        return (EditText) ((TableRow) matrixTable.getChildAt(rowIndex)).getChildAt(columnIndex);

    }


    private Matrix setupMatrix() {
        List<ComplexNumber> matrixList = new ArrayList<>();
        List<ComplexNumber> constantsList = new ArrayList<>();
        try {
            // filling the matrixList from matrixTable
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    String s = ((EditText) (((TableRow) matrixTable.getChildAt(i)).
                            getChildAt(j))).getText().toString();
                    ComplexNumber complexNumber = new ComplexNumber(s);
                    matrixList.add(complexNumber);

                }
            }
            // filling the constants list from constantsTable
            for (int i = 0; i < 3; i++) {
                String s = ((EditText) (((TableRow) constantsTable.getChildAt(i)).
                        getChildAt(0))).getText().toString();
                ComplexNumber complexNumber = new ComplexNumber(s);
                constantsList.add(complexNumber);

            }
        } catch (NumberFormatException e){
            // if one of the numbers is not valid we inform user and return null
            solutionTextView.setText(" Invalid Input ");
            solutionTextView.setTextSize(25);
            isDisplayingError = true;
            return null;
        }
        return new Matrix(matrixList, constantsList);
    }

    private void solveSystemOfEquation() {
        Matrix matrix = setupMatrix();
        ComplexNumber[] result;
        if ( matrix != null ) {
            try {
                result = matrix.solveSystemOfEquation();
                String solution = "X = " + result[0].toString() + " , Y = " + result[1].toString() + " , Z = " + result[2].toString();
                solutionTextView.setText(solution);
                solutionTextView.setTextSize(18);
            } catch (ArithmeticException exception) {
                // catch exception thrown from solveSystemOfEquation as a result of attempting to divide by zero
                solutionTextView.setText(" The determinant value can't be zero ");
                solutionTextView.setTextSize(25f);
                isDisplayingError = true;
            }
        }
    }
}
