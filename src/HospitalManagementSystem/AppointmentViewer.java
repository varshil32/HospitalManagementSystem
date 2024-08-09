package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentViewer {
    private Connection connection;

    public AppointmentViewer(Connection connection) {
        this.connection = connection;
    }

    public void viewAppointments() {
        String query = "SELECT * FROM appointments";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Appointments: ");
            System.out.println("+------------+------------+------------------+");
            System.out.println("| Patient ID | Doctor ID  | Appointment Date |");
            System.out.println("+------------+------------+------------------+");
            while (resultSet.next()) {
                int patientId = resultSet.getInt("patient_id");
                int doctorId = resultSet.getInt("doctor_id");
                String appointmentDate = resultSet.getString("appointment_date");
                System.out.printf("| %-10s | %-10s | %-16s |\n", patientId, doctorId, appointmentDate);
                System.out.println("+------------+------------+------------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
