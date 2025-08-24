package main.utils;

import main.model.DrivingExam;

// used by the SetExamResultsController to store the exams to be evaluated by officer
public class ExamStack {
    private DrivingExam[] stack;
    private int top;
    private int size;

    public ExamStack(int size) {
        stack = new DrivingExam[size];
        top = -1;
        this.size = size;
    }

    public void push(DrivingExam question) {
        if (top == stack.length - 1) {
            resize();
        }
        stack[++top] = question;
    }

    public DrivingExam pop() {
        if (top == -1) {
            throw new RuntimeException("Stack Underflow");
        }
        return stack[top--];
    }

    // logic to resize stack if stack overflows
    private void resize() {
        size = size*2;
        DrivingExam[] new_stack = new DrivingExam[size];
        for (int i=0; i<stack.length; i++) {
            new_stack[i] = stack[i];
        }
        this.stack = new_stack;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top + 1;
    }
}