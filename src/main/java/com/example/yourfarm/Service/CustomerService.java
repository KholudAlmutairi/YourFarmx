package com.example.yourfarm.Service;

import com.example.yourfarm.API.ApiException;
import com.example.yourfarm.DTO.CustomerDTO;
import com.example.yourfarm.DTO.PlantDTO;
import com.example.yourfarm.DTO.PlantsDTO;
import com.example.yourfarm.Model.*;
import com.example.yourfarm.Repository.*;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AuthRepository authRepository;
    private final AuthService authService;
    private final OrderPlantRepository orderPlantRepository;
    private final OrderFarmerRepository orderFarmerRepository;
    private final OrderGuidanceRepository orderGuidanceRepository;
    private final PlantRepository plantRepository;
    private final FarmRepository farmRepository;
    private final FarmerRepository farmerRepository;
    private final SpecialistRepository specialistRepository;

    //ADMIN
    public List<Customer> getAllCustomer() {
        if (customerRepository.findAll().isEmpty())
            throw new ApiException("EmptyList");
        else return customerRepository.findAll();
    }

    //CUSTOMER
    public void register(CustomerDTO customer) {
        User user = new User(null, customer.getUserName(), customer.getPassword(), "CUSTOMER", customer.getName(), customer.getEmail(), customer.getPhoneNumber(), null, null, null, null, null);
        authService.register(user);

        Customer customer1 = new Customer(user.getId(), null, null, null, user);
        customerRepository.save(customer1);
        user.setCustomer(customer1);

    }

    //CUSTOMER
    public void update(Integer customerId, CustomerDTO customer) {
        User user = authRepository.findUserById(customerId);
        Customer customer1 = customerRepository.findCustomerById(customerId);

        user.setName(customer.getName());
        user.setEmail(customer.getEmail());
        user.setPhoneNumber(customer.getPhoneNumber());
        user.setPassword(customer.getPassword());
        user.setUsername(customer.getUserName());
        authRepository.save(user);

        customer1.setUser(user);
        customerRepository.save(customer1);
    }

    //ADMIN
    public void deleteCustomer(Integer customerId) {
        Customer customer1 = customerRepository.findCustomerById(customerId);
        if (customer1 == null) {
            throw new ApiException(" Customer not found");
        }
        customerRepository.delete(customer1);
    }

    //-------------------------------------   end CRUD  ---------------------------

    public List<OrderPlant> currentPlantOrders(Integer customerId) {
        ArrayList<OrderPlant> orders2 = new ArrayList<>();

        List<OrderPlant> orders3 = orderPlantRepository.findOrdersByCustomerId(customerId);
        if (orders3 == null) {
            throw new ApiException(" orders not found");
        }
        for (OrderPlant orders1 : orders3) {
            if (orders1.getStatus().equalsIgnoreCase("Ready to deliver") || orders1.getStatus().equalsIgnoreCase("accepted") || orders1.getStatus().equalsIgnoreCase("Waiting")) {
                orders2.add(orders1);
            }
        }

        return orders2;
    }


    public List<OrderPlant> previousPlantOrders(Integer customerId) {
        ArrayList<OrderPlant> orders2 = new ArrayList<>();
        List<OrderPlant> orders3 = orderPlantRepository.findOrdersByCustomerId(customerId);

        if (orders3 == null) {
            throw new ApiException(" orders not found");
        }
        for (OrderPlant orders1 : orders3) {
            if (orders1.getStatus().equalsIgnoreCase("Delivered") || orders1.getStatus().equalsIgnoreCase("Rejected")) {
                orders2.add(orders1);
            }
        }

        return orders2;
    }

    public List<OrderFarmer> FarmerOrders(Integer customerId) {
        ArrayList<OrderFarmer> orders2 = new ArrayList<>();
        List<OrderFarmer> orders3 = orderFarmerRepository.findOrderFarmerByCustomerId(customerId);

        if (orders3 == null) {
            throw new ApiException(" orders not found");
        }


        return orders2;
    }

    public List<OrderGuidance> GuidanceOrders(Integer customerId) {
        ArrayList<OrderGuidance> orders2 = new ArrayList<>();
        List<OrderGuidance> orders3 = orderGuidanceRepository.findOrderGuidanceByCustomerId(customerId);

        if (orders3 == null) {
            throw new ApiException(" orders not found");
        }


        return orders2;
    }

//    public List<OrderGuidance> GuidanceOrders(Integer customerId, PlantDTO plantDTO) {
//
//        OrderPlant orderPlant = new OrderPlant();
//        orderPlant.setReceivedDate(plantDTO.getReceivedDate());
//        orderPlant.setTimeOfReceive(plantDTO.getTimeOfReceive());
//        PlantsDTO plantsDTO = plantDTO.getPlantsDTOS().get(1);
//        plantsDTO.getId();
//        plantRepository.findPlantById(plantsDTO.getId());
//    }


    ///////////////////////1
    public List<Farm> searchFarmsNear(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);

        if (customer == null) {
            throw new ApiException("Customer ID not found");
        }


        String customerDistrict = customer.getDistrict();
        List<Farm> farmms1 = farmRepository.findAllByRegion(customerDistrict);
        if(farmms1.isEmpty()){
            throw new ApiException("The list is empty");
        }

        return farmms1;

    }



    ///////////////////////2

    public List<Farmer> searchFarmerNear(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);

        if (customer == null) {
            throw new ApiException("Customer ID not found");
        }

        String customerDistrict = customer.getDistrict();
        List<Farmer> farmers = farmerRepository.findAllByRegion(customerDistrict);

        return farmers;


    }

    ///////////////////////3
    public List<Specialist> searchSpecialistsNear(Integer customerId) {
        Customer customer = customerRepository.findCustomerById(customerId);

        if (customer == null) {
            throw new ApiException("Customer ID not found");
        }

        String customerDistrict = customer.getDistrict();
        List<Specialist> specialists = specialistRepository.findAllByRegion(customerDistrict);

        return specialists;
    }



}





