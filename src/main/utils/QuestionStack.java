package main.utils;

import main.model.DrivingExamQuestions;

public class QuestionStack {
    private DrivingExamQuestions[] stack;
    private int top;

    public QuestionStack(int size) {
        stack = new DrivingExamQuestions[size];
        top = -1;
    }

    public void push(DrivingExamQuestions question) {
        if (top == stack.length - 1) {
            throw new RuntimeException("Stack Overflow");
        }
        stack[++top] = question;
    }

    public DrivingExamQuestions pop() {
        if (top == -1) {
            throw new RuntimeException("Stack Underflow");
        }
        return stack[top--];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top + 1;
    }
}