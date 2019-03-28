package com.example.manasatpc.bloadbank.u.ui.fregmants.homeCycle.regusets;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manasatpc.bloadbank.R;
import com.example.manasatpc.bloadbank.u.data.rest.APIServices;
import com.example.manasatpc.bloadbank.u.data.model.donation.donationrequestcreate.DonationRequestCreate;
import com.example.manasatpc.bloadbank.u.data.model.general.bloodtypes.BloodTypes;
import com.example.manasatpc.bloadbank.u.data.model.general.bloodtypes.DataBloodTypes;
import com.example.manasatpc.bloadbank.u.data.model.general.cities.Cities;
import com.example.manasatpc.bloadbank.u.data.model.general.cities.DataCities;
import com.example.manasatpc.bloadbank.u.data.model.general.governorates.Governorates;
import com.example.manasatpc.bloadbank.u.data.model.general.governorates.GovernoratesData;
import com.example.manasatpc.bloadbank.u.helper.HelperMethod;
import com.example.manasatpc.bloadbank.u.helper.SaveData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.manasatpc.bloadbank.u.data.rest.RetrofitClient.getRetrofit;
import static com.example.manasatpc.bloadbank.u.helper.HelperMethod.GET_DATA;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDonationFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.ET_Name_Patient)
    TextInputEditText ETNamePatient;
    @BindView(R.id.ET_Age_Patient)
    TextInputEditText ETAgePatient;
    @BindView(R.id.Spinner_Blood_Types_Request_Donation)
    Spinner SpinnerBloodTypesRequestDonation;
    @BindView(R.id.Spinner_Number_Package)
    Spinner SpinnerNumberPackage;
    @BindView(R.id.ET_Name_Hospital_Patient)
    TextInputEditText ETNameHospitalPatient;
    @BindView(R.id.Fragment_Adrees_Hospital)
    TextView FragmentAdreesHospital;
    @BindView(R.id.Spinner_Gaverment_Request_Donation)
    Spinner SpinnerGavermentRequestDonation;
    @BindView(R.id.Spinner_City_Request_Donation)
    Spinner SpinnerCityRequestDonation;
    @BindView(R.id.ET_Phone_Patient)
    TextInputEditText ETPhonePatient;
    @BindView(R.id.ET_Notes_Patient)
    TextInputEditText ETNotesPatient;
    @BindView(R.id.BT_Send_Request)
    Button BTSendRequest;
    private SaveData saveData;
    private long number_package;
    ArrayList<Integer> number_package_blood = new ArrayList<>();
    private APIServices apiServicesgetGovernorate;
    String getResult;
    int IDPosition;
    Integer positionCity;
    Integer positionBloodType;
    int blood_type;
    ArrayList<String> strings = new ArrayList<>();
    final ArrayList<Integer> Ids = new ArrayList<>();
    final ArrayList<Integer> IdsCity = new ArrayList<>();
    final ArrayList<Integer> IdsBloodType = new ArrayList<>();

    public RequestDonationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_donation, container, false);
        unbinder = ButterKnife.bind(this, view);
        saveData = getArguments().getParcelable(GET_DATA);
        getBloodTypes();

        //strings.add(getString(R.string.select_number_package));
        for (int i = 1; i < 10; i++) {
            number_package_blood.add(i);
        }
        //For fill SpinnerNumberPackage
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(),
                android.R.layout.simple_spinner_item, number_package_blood);

        SpinnerNumberPackage.setAdapter(adapter);

        SpinnerNumberPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //      Toast.makeText(getActivity(), "Position :" + position + "id:" + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // for get DataPost to governorate Spinner
        apiServicesgetGovernorate = getRetrofit().create(APIServices.class);
        final Call<Governorates> governorates = apiServicesgetGovernorate.getGovernorates();
        governorates.enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                if (response.body().getStatus() == 1) {
                    String getResult;
                    ArrayList<String> strings = new ArrayList<>();
                    final ArrayList<Integer> Ids = new ArrayList<>();

                    try {
                        Governorates governorates1 = response.body();

                        strings.add(getString(R.string.select_gaverment));
                        Ids.add(0);

                        List<GovernoratesData> governoratesData = governorates1.getData();
                        for (int i = 0; i < governoratesData.size(); i++) {
                            getResult = governoratesData.get(i).getName();
                            strings.add(getResult);
                            Ids.add(governoratesData.get(i).getId());
                        }
                        HelperMethod.showGovernorates(strings, getActivity(), SpinnerGavermentRequestDonation);
                        SpinnerGavermentRequestDonation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {
                                    getCities(Ids.get(i));
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<Governorates> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        return view;
    }

    private void getCities(int getIdGovernorates) {
        APIServices apiServicesgetCities = getRetrofit().create(APIServices.class);
        final Call<Cities> citiesCall = apiServicesgetCities.getCities(getIdGovernorates);
        citiesCall.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                String getResult;
                ArrayList<String> strings = new ArrayList<>();

                try {
                    strings.add(getString(R.string.select_city));
                    IdsCity.add(0);

                    Cities cities = response.body();

                    List<DataCities> dataCities = cities.getData();
                    for (int i = 0; i < dataCities.size(); i++) {
                        getResult = dataCities.get(i).getName();
                        strings.add(getResult);
                        positionCity = dataCities.get(i).getId();
                        IdsCity.add(positionCity);

                   }

                    HelperMethod.showGovernorates(strings, getActivity(), SpinnerCityRequestDonation);

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void getBloodTypes() {
        APIServices apiServicesgetBloodTypes = getRetrofit().create(APIServices.class);
        final Call<BloodTypes> bloodTypesCall = apiServicesgetBloodTypes.getBloodTypes();
        bloodTypesCall.enqueue(new Callback<BloodTypes>() {
            @Override
            public void onResponse(Call<BloodTypes> call, Response<BloodTypes> response) {
                String getResult;
                ArrayList<String> strings = new ArrayList<>();
                BloodTypes bloodTypes = response.body();
                if (bloodTypes.getStatus() == 1) {
                    try {
                        strings.add(getString(R.string.blood_type));
                        IdsBloodType.add(0);

                        List<DataBloodTypes> bloodTypesList = bloodTypes.getData();
                        for (int i = 0; i < bloodTypesList.size(); i++) {
                            getResult = bloodTypesList.get(i).getName();
                            strings.add(getResult);
                            positionBloodType = bloodTypesList.get(i).getId();
                            IdsBloodType.add(positionBloodType);

                        }

                        HelperMethod.showGovernorates(strings, getActivity(), SpinnerBloodTypesRequestDonation);

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<BloodTypes> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.BT_Send_Request)
    public void onViewClicked() {
        String name_patient = ETNamePatient.getText().toString().trim();
        String age_patient = ETAgePatient.getText().toString().trim();
        int blood_type = SpinnerBloodTypesRequestDonation.getSelectedItemPosition();
        String hospital_name = ETNameHospitalPatient.getText().toString().trim();
        String phone = ETPhonePatient.getText().toString().trim();
        String notes = ETNotesPatient.getText().toString().trim();
        String number_package_string = String.valueOf(number_package);

        String hospital_address = ETNamePatient.getText().toString().trim();
        ;
        int city_id = SpinnerCityRequestDonation.getSelectedItemPosition();
        ;
        String longtude = ETNamePatient.getText().toString().trim();
        ;
        String latitude = ETNamePatient.getText().toString().trim();
        ;
        apiServicesgetGovernorate.getDonationRequestCreate(saveData.getApi_token(), name_patient, age_patient
                , blood_type, number_package_string, hospital_name, hospital_address, city_id, phone, notes, latitude, longtude).enqueue(new Callback<DonationRequestCreate>() {
            @Override
            public void onResponse(Call<DonationRequestCreate> call, Response<DonationRequestCreate> response) {
                DonationRequestCreate donationRequestCreate = response.body();
                if (donationRequestCreate.getStatus() == 1) {
                    Toast.makeText(getActivity(), donationRequestCreate.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), donationRequestCreate.getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<DonationRequestCreate> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @OnClick({R.id.Fragment_Adrees_Hospital, R.id.BT_Send_Request})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Fragment_Adrees_Hospital:
                MapFragment mapFragment = new MapFragment();
                HelperMethod.replece(mapFragment, getActivity().getSupportFragmentManager(),
                        R.id.Cycle_Home_contener, null, null, saveData);
                break;
            case R.id.BT_Send_Request:
                break;
        }
    }
}
