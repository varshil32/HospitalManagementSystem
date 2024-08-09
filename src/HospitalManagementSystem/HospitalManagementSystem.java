package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String RECEPTIONIST_USERNAME = "recep";
    private static final String RECEPTIONIST_PASSWORD = "recep123";
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Varshil@1";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);

        String role = authenticateUser(scanner);

        if (role != null) {
            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                Patient patient = new Patient(connection, scanner);
                Doctor doctor = new Doctor(connection);
                AppointmentViewer appointmentViewer = new AppointmentViewer(connection);
                while (true) {
                    System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
                    if (role.equals("admin")) {
                        System.out.println("1. Add Patient");
                        System.out.println("2. View Patients");
                        System.out.println("3. View Doctors");
                        System.out.println("4. Book Appointment");
                        System.out.println("5. View Appointments");
                        System.out.println("6. Delete Patient");
                        System.out.println("7. Delete Doctor");
                        System.out.println("8. Add Doctor");
                        System.out.println("9. Delete Appointment");
                        System.out.println("10. Exit");
                    } else if (role.equals("receptionist")) {
                        System.out.println("1. Add Patient");
                        System.out.println("2. View Patients");
                        System.out.println("3. View Doctors");
                        System.out.println("4. Book Appointment");
                        System.out.println("5. View Appointments");
                        System.out.println("6. Exit");
                    }
                    System.out.println("Enter your choice: ");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            // Add Patient
                            patient.addPatient();
                            System.out.println();
                            break;
                        case 2:
                            // View Patient
                            patient.viewPatients();
                            System.out.println();
                            break;
                        case 3:
                            // View Doctors
                            doctor.viewDoctors();
                            System.out.println();
                            break;
                        case 4:
                            // Book Appointment
                            bookAppointment(patient, doctor, connection, scanner);
                            System.out.println();
                            break;
                        case 5:
                            // View Appointments
                            appointmentViewer.viewAppointments();
                            System.out.println();
                            break;
                        case 6:
                            if (role.equals("admin")) {
                                // Delete Patient
                                deletePatient(patient, connection, scanner);
                                System.out.println();
                            } else {
                                System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM!!");
                                return;
                            }
                            break;
                        case 7:
                            if (role.equals("admin")) {
                                // Delete Doctor
                                deleteDoctor(doctor, connection, scanner);
                                System.out.println();
                            } else {
                                System.out.println("Invalid choice for receptionist!");
                            }
                            break;
                        case 8:
                            if (role.equals("admin")) {
                                // Add Doctor
                                addDoctor(doctor, connection, scanner);
                                System.out.println();
                            } else {
                                System.out.println("Invalid choice for receptionist!");
                            }
                            break;
                        case 9:
                            if (role.equals("admin")) {
                                // Delete Appointment
                                deleteAppointment(connection, scanner);
                                System.out.println();
                            } else {
                                System.out.println("Invalid choice for receptionist!");
                            }
                            break;
                        case 10:
                            System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM!!");
                            return;
                        default:
                            System.out.println("Enter valid choice!!!");
                            break;
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid username or password. Exiting...");
        }
    }


    private static String authenticateUser(Scanner scanner) {
        System.out.println("Welcome to Hospital Management System!");
        System.out.print("Enter Username: ");
        String username = scanner.next();
        System.out.print("Enter Password: ");
        String password = scanner.next();

        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return "admin";
        } else if (username.equals(RECEPTIONIST_USERNAME) && password.equals(RECEPTIONIST_PASSWORD)) {
            return "receptionist";
        } else {
            return null;
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner){
        System.out.print("Enter Patient Id: ");
        int patientId = scanner.nextInt();
        System.out.print("Enter Doctor Id: ");
        int doctorId = scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String appointmentDate = scanner.next();
        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId, appointmentDate, connection)){
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setString(3, appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment Booked!");
                    }else{
                        System.out.println("Failed to Book Appointment!");
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor not available on this date!!");
            }
        }else{
            System.out.println("Either doctor or patient doesn't exist!!!");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection){
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static void deletePatient(Patient patient, Connection connection, Scanner scanner) {
        System.out.print("Enter Patient Id to delete: ");
        int patientId = scanner.nextInt();
        if (patient.getPatientById(patientId)) {
            try {
                String query = "DELETE FROM patients WHERE id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, patientId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Patient deleted successfully!");
                } else {
                    System.out.println("Failed to delete patient!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Patient with provided ID does not exist!");
        }
    }

    public static void deleteDoctor(Doctor doctor, Connection connection, Scanner scanner) {
        System.out.print("Enter Doctor Id to delete: ");
        int doctorId = scanner.nextInt();
        if (doctor.getDoctorById(doctorId)) {
            try {
                String query = "DELETE FROM doctors WHERE id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, doctorId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Doctor deleted successfully!");
                } else {
                    System.out.println("Failed to delete doctor!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Doctor with provided ID does not exist!");
        }
    }

    public static void deleteAppointment(Connection connection, Scanner scanner) {
        System.out.print("Enter Appointment Id to delete: ");
        int appointmentId = scanner.nextInt();
        try {
            String query = "DELETE FROM appointments WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, appointmentId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment deleted successfully!");
            } else {
                System.out.println("Failed to delete appointment!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void addDoctor(Doctor doctor, Connection connection, Scanner scanner) {
        System.out.print("Enter Doctor Name: ");
        String name = scanner.next();
        System.out.print("Enter Doctor Specialization: ");
        String specialization = scanner.next();
        try {
            String query = "INSERT INTO doctors(name, specialization) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, specialization);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Doctor added successfully!");
            } else {
                System.out.println("Failed to add doctor!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
