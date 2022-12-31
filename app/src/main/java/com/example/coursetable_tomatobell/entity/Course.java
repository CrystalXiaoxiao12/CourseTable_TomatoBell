package com.example.coursetable_tomatobell.entity;

import java.util.ArrayList;

public class Course {
    private String courseName;
    private String teacher;
    private String classRoom;
    private int day;
    private int classStart;
    private int classEnd;


    public Course(String courseName, String teacher, String classRoom, int day, int classStart, int classEnd) {
        this.courseName = courseName;
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.day = day;
        this.classStart = classStart;
        this.classEnd = classEnd;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStart() {
        return classStart;
    }

    public void setStart(int classStart) {
        this.classEnd = classStart;
    }

    public int getEnd() {
        return classEnd;
    }

    public void setEnd(int classEnd) {
        this.classEnd = classEnd;
    }

    public ArrayList<String> toArrayList(){
        ArrayList<String> temp=new ArrayList<String>();
        temp.add(courseName);
        temp.add(teacher);
        temp.add(classRoom);
        temp.add(String.valueOf(day));
        temp.add(String.valueOf(classStart));
        temp.add(String.valueOf(classEnd));
        return  temp;
    }

}
