package com.shiftmanager.api.config;

import com.shiftmanager.api.model.*;
import com.shiftmanager.api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * Configuration to load initial data for testing
 */
@Configuration
public class InitialDataLoader {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Initialize sample data for development or testing
     * Only runs in dev or test profiles
     * @param roleRepository Role repository
     * @param userRepository User repository
     * @param locationRepository Location repository
     * @param shiftTypeRepository Shift type repository
     * @param personRepository Person repository
     * @param employeeRepository Employee repository
     * @return CommandLineRunner
     */
    @Bean
    @Profile({"dev", "test"})
    public CommandLineRunner initialData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            LocationRepository locationRepository,
            ShiftTypeRepository shiftTypeRepository,
            PersonRepository personRepository,
            EmployeeRepository employeeRepository) {

        return args -> {
            // Only initialize if no roles exist (first run)
            if (roleRepository.count() == 0) {
                // Create roles
                Role adminRole = roleRepository.save(new Role("ROLE_ADMIN", "Administrator"));
                Role managerRole = roleRepository.save(new Role("ROLE_MANAGER", "Manager"));
                Role employeeRole = roleRepository.save(new Role("ROLE_EMPLOYEE", "Employee"));

                // Create locations
                Location mainOffice = new Location();
                mainOffice.setName("Main Office");
                mainOffice.setAddress("123 Main St");
                mainOffice.setCity("Boston");
                mainOffice.setState("MA");
                mainOffice.setZipCode("02108");
                mainOffice.setPhoneNumber("617-555-1234");
                locationRepository.save(mainOffice);

                Location downtown = new Location();
                downtown.setName("Downtown Store");
                downtown.setAddress("456 Market St");
                downtown.setCity("Boston");
                downtown.setState("MA");
                downtown.setZipCode("02109");
                downtown.setPhoneNumber("617-555-5678");
                locationRepository.save(downtown);

                // Create shift types
                ShiftType morningShift = new ShiftType();
                morningShift.setName("Morning");
                morningShift.setStartTime(LocalTime.of(8, 0));
                morningShift.setEndTime(LocalTime.of(16, 0));
                morningShift.setColor("#4CAF50"); // Green
                shiftTypeRepository.save(morningShift);

                ShiftType eveningShift = new ShiftType();
                eveningShift.setName("Evening");
                eveningShift.setStartTime(LocalTime.of(16, 0));
                eveningShift.setEndTime(LocalTime.of(0, 0));
                eveningShift.setColor("#2196F3"); // Blue
                shiftTypeRepository.save(eveningShift);

                ShiftType nightShift = new ShiftType();
                nightShift.setName("Night");
                nightShift.setStartTime(LocalTime.of(0, 0));
                nightShift.setEndTime(LocalTime.of(8, 0));
                nightShift.setColor("#9C27B0"); // Purple
                shiftTypeRepository.save(nightShift);

                // Create a manager
                Manager manager = new Manager();
                manager.setFirstName("John");
                manager.setLastName("Smith");
                manager.setEmail("john.smith@example.com");
                manager.setPhoneNumber("617-555-1111");
                manager.setEmployeeId("EMP001");
                manager.setHireDate(LocalDate.of(2015, 1, 15));
                manager.setJobTitle("Store Manager");
                manager.setDepartment("Operations");
                manager.setStatus("ACTIVE");
                manager.setLocation(mainOffice);
                manager.setManagementLevel("Senior");
                employeeRepository.save(manager);

                // Create employees
                Employee employee1 = new Employee();
                employee1.setFirstName("Jane");
                employee1.setLastName("Doe");
                employee1.setEmail("jane.doe@example.com");
                employee1.setPhoneNumber("617-555-2222");
                employee1.setEmployeeId("EMP002");
                employee1.setHireDate(LocalDate.of(2018, 3, 20));
                employee1.setJobTitle("Sales Associate");
                employee1.setDepartment("Sales");
                employee1.setStatus("ACTIVE");
                employee1.setLocation(downtown);
                employee1.setManager(manager);
                employeeRepository.save(employee1);

                Employee employee2 = new Employee();
                employee2.setFirstName("Bob");
                employee2.setLastName("Johnson");
                employee2.setEmail("bob.johnson@example.com");
                employee2.setPhoneNumber("617-555-3333");
                employee2.setEmployeeId("EMP003");
                employee2.setHireDate(LocalDate.of(2020, 6, 10));
                employee2.setJobTitle("Cashier");
                employee2.setDepartment("Sales");
                employee2.setStatus("ACTIVE");
                employee2.setLocation(downtown);
                employee2.setManager(manager);
                employeeRepository.save(employee2);

                // Create users
                User adminUser = new User("admin", "admin@example.com", passwordEncoder.encode("password"));
                adminUser.addRole(adminRole);
                adminUser.addRole(managerRole);
                adminUser.addRole(employeeRole);
                adminUser.setEmployee(manager);
                userRepository.save(adminUser);

                User user1 = new User("jdoe", "jane.doe@example.com", passwordEncoder.encode("password"));
                user1.addRole(employeeRole);
                user1.setEmployee(employee1);
                userRepository.save(user1);

                User user2 = new User("bjohnson", "bob.johnson@example.com", passwordEncoder.encode("password"));
                user2.addRole(employeeRole);
                user2.setEmployee(employee2);
                userRepository.save(user2);

                // Create shifts and assign them
                // To be implemented
            }
        };
    }
}
