package servlet;

import dao.*;
import model.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Time;

public class AddTimetableServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            SubjectDAO subjectDAO = new SubjectDAO();
            TeacherDAO teacherDAO = new TeacherDAO();
            ClassDAO classDAO = new ClassDAO();

            // 🔥 GET classId from request
            String classParam = request.getParameter("classId");

            if (classParam != null && !classParam.isEmpty()) {

                int classId = Integer.parseInt(classParam);

                // ✅ FILTERED SUBJECTS
                request.setAttribute("subjects", subjectDAO.getSubjectsByClassId(classId));

            } else {

                // 👉 FIRST LOAD (no class selected)
                request.setAttribute("subjects", new java.util.ArrayList<>());
            }

            request.setAttribute("teachers", teacherDAO.getAllTeachers());
            request.setAttribute("classes", classDAO.getAllClasses());

            request.getRequestDispatcher("admin_timetable.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("ERROR: " + e.getMessage());
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String classParam = request.getParameter("classId");

            if (classParam == null || classParam.isEmpty()) {
                request.setAttribute("error", "Class is required");
                doGet(request, response);
                return;
            }

            int classId = Integer.parseInt(classParam);

            TimetableDAO dao = new TimetableDAO();

            String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

            boolean hasError = false;
            String errorMessage = "";

            // 🔴 IMPORTANT: Track same-form conflicts
            java.util.Set<String> teacherSlots = new java.util.HashSet<>();

            // =========================
            // 1. VALIDATION PHASE
            // =========================
            for (String day : days) {

                for (int i = 1; i <= 6; i++) {

                    String subjectId = request.getParameter(day + "_subject_" + i);
                    String teacherId = request.getParameter(day + "_teacher_" + i);
                    String start = request.getParameter(day + "_start_" + i);
                    String end = request.getParameter(day + "_end_" + i);

                    // if any field is filled → all required
                    if ((subjectId != null && !subjectId.isEmpty()) ||
                            (teacherId != null && !teacherId.isEmpty()) ||
                            (start != null && !start.isEmpty()) ||
                            (end != null && !end.isEmpty())) {

                        if (subjectId == null || subjectId.isEmpty()
                                || teacherId == null || teacherId.isEmpty()
                                || start == null || start.isEmpty()
                                || end == null || end.isEmpty()) {

                            hasError = true;
                            errorMessage = "Complete all fields for " + day + " period " + i;
                            break;
                        }

                        // time validation
                        if (start.compareTo(end) >= 0) {
                            hasError = true;
                            errorMessage = "Invalid time (start >= end) in " + day + " period " + i;
                            break;
                        }

                        int tId = Integer.parseInt(teacherId);

                        // 🔥 SAME FORM CONFLICT CHECK
                        String slotKey = tId + "_" + day + "_" + i;

                        if (teacherSlots.contains(slotKey)) {
                            hasError = true;
                            errorMessage = "Same teacher assigned multiple times in form: " + day + " period " + i;
                            break;
                        }

                        teacherSlots.add(slotKey);

                        // 🔥 DATABASE CONFLICT CHECK
                        if (dao.isTeacherBusy(tId, day, i)) {
                            hasError = true;
                            errorMessage = "Teacher already assigned in DB: " + day + " period " + i;
                            break;
                        }
                    }
                }

                if (hasError) break;
            }

            // stop if error
            if (hasError) {
                request.setAttribute("error", errorMessage);
                doGet(request, response);
                return;
            }

            // =========================
            // 2. SAVE PHASE
            // =========================
            for (String day : days) {

                dao.deleteByClassAndDay(classId, day);

                for (int i = 1; i <= 6; i++) {

                    String subjectId = request.getParameter(day + "_subject_" + i);
                    String teacherId = request.getParameter(day + "_teacher_" + i);

                    if (subjectId != null && !subjectId.isEmpty()
                            && teacherId != null && !teacherId.isEmpty()) {

                        TimetableModel t = new TimetableModel();

                        t.setClassId(classId);
                        t.setDay(day);
                        t.setPeriodNumber(i);
                        t.setSubjectId(Integer.parseInt(subjectId));
                        t.setTeacherId(Integer.parseInt(teacherId));

                        String start = request.getParameter(day + "_start_" + i);
                        String end = request.getParameter(day + "_end_" + i);

                        if (start != null && end != null && !start.isEmpty() && !end.isEmpty()) {
                            t.setStartTime(Time.valueOf(start + ":00"));
                            t.setEndTime(Time.valueOf(end + ":00"));
                        }

                        dao.addTimetable(t);
                    }
                }
            }

            // success
            request.setAttribute("success", "Timetable saved successfully!");
            doGet(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "System error: " + e.getMessage());
            doGet(request, response);
        }
    }



}

