package com.example.manasatpc.bloadbank.u.ui.fregmants.homeCycle;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manasatpc.bloadbank.R;
import com.example.manasatpc.bloadbank.u.data.model.general.bloodtypes.BloodTypes;
import com.example.manasatpc.bloadbank.u.data.model.general.bloodtypes.DataBloodTypes;
import com.example.manasatpc.bloadbank.u.data.model.general.cities.Cities;
import com.example.manasatpc.bloadbank.u.data.model.general.cities.DataCities;
import com.example.manasatpc.bloadbank.u.data.model.general.governorates.Governorates;
import com.example.manasatpc.bloadbank.u.data.model.general.governorates.GovernoratesData;
import com.example.manasatpc.bloadbank.u.data.model.user.editprofile.EditProfile;
import com.example.manasatpc.bloadbank.u.data.model.user.getprofile.ClientGetProfile;
import com.example.manasatpc.bloadbank.u.data.model.user.getprofile.GetProfile;
import com.example.manasatpc.bloadbank.u.data.rest.APIServices;
import com.example.manasatpc.bloadbank.u.helper.DateModel;
import com.example.manasatpc.bloadbank.u.helper.HelperMethod;
import com.example.manasatpc.bloadbank.u.helper.RememberMy;
import com.example.manasatpc.bloadbank.u.helper.SaveData;
import com.example.manasatpc.bloadbank.u.ui.fregmants.homeCycle.article.HomeFragment;

import java.util.ArrayList;
import java.util.Calendar;
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
import static com.example.manasatpc.bloadbank.u.ui.activities.HomeActivity.toolbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditInformationFragment extends Fragment {
    @BindView(R.id.EditInformationFragment_Name)
    TextInputEditText EditInformationFragmentName;
    @BindView(R.id.EditInformationFragment_Email)
    TextInputEditText EditInformationFragmentEmail;
    @BindView(R.id.EditInformationFragment_Date_Of_Birth)
    TextView EditInformationFragmentDateOfBirth;
    @BindView(R.id.EditInformationFragment_SP_Blood_Type)
    Spinner EditInformationFragmentSPBloodType;
    @BindView(R.id.EditInformationFragment_Last_Date_Donation)
    TextView EditInformationFragmentLastDateDonation;
    @BindView(R.id.EditInformationFragment_SP_Gaverment)
    Spinner EditInformationFragmentSPGaverment;
    @BindView(R.id.EditInformationFragment_SP_City)
    Spinner EditInformationFragmentSPCity;
    @BindView(R.id.EditInformationFragment_Phone)
    TextInputEditText EditInformationFragmentPhone;
    @BindView(R.id.EditInformationFragment_Password)
    TextInputEditText EditInformationFragmentPassword;
    @BindView(R.id.EditInformationFragment_Retry_Password)
    TextInputEditText EditInformationFragmentRetryPassword;
    @BindView(R.id.EditInformationFragment_BT_Regester)
    Button EditInformationFragmentBTRegester;
    @BindView(R.id.EditInformationFragment_Progress_Bar)
    ProgressBar EditInformationFragmentProgressBar;
    Unbinder unbinder;
    private APIServices apiServices;
    boolean check_network;
    private DateModel dateModel1;
    private DateModel dateModel2;
    final Calendar getDatenow = Calendar.getInstance();
    private int startYear;
    private int startMonth;
    private int startDay;
    String getResult;
    RememberMy rememberMy;
    Integer positionGaverment;
    Integer positionCity;
    Integer positionBloodType;
    ArrayList<String> stringsBloodType = new ArrayList<>();
    ArrayList<Integer> IdsCity = new ArrayList<>();
    final ArrayList<Integer> IdsBloodType = new ArrayList<>();
    final ArrayList<Integer> IdsGeverment = new ArrayList<>();
    ArrayList<String> stringsGaverment = new ArrayList<>();

    private ClientGetProfile clientGetProfile;

    public EditInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_information, container, false);
        unbinder = ButterKnife.bind(this, view);
        rememberMy = new RememberMy(getActivity());
        check_network = HelperMethod.isNetworkConnected(getActivity(), getView());
        startYear = getDatenow.get(Calendar.YEAR);
        startMonth = getDatenow.get(Calendar.MONTH);
        startDay = getDatenow.get(Calendar.DAY_OF_MONTH);
        dateModel1 = new DateModel(String.valueOf(startYear), String.valueOf(startMonth)
                , String.valueOf(startDay), null);
        dateModel2 = new DateModel(String.valueOf(startYear), String.valueOf(startMonth)
                , String.valueOf(startDay), null);
        if (check_network == false) {
            EditInformationFragmentProgressBar.setVisibility(View.GONE);
        }

        apiServices = getRetrofit().create(APIServices.class);
        EditInformationFragmentProgressBar.setVisibility(View.VISIBLE);
        apiServices.getProfile(rememberMy.getAPIKey()).enqueue(new Callback<GetProfile>() {
            @Override
            public void onResponse(Call<GetProfile> call, Response<GetProfile> response) {
                GetProfile getProfile = response.body();
                clientGetProfile = getProfile.getData().getClient();
                try {
                    if (getProfile.getStatus() == 1) {
                        EditInformationFragmentProgressBar.setVisibility(View.GONE);
                        EditInformationFragmentDateOfBirth.setText(clientGetProfile.getBirthDate());
                        EditInformationFragmentEmail.setText(clientGetProfile.getEmail());
                        EditInformationFragmentLastDateDonation.setText(clientGetProfile.getDonationLastDate());
                        EditInformationFragmentName.setText(clientGetProfile.getName());
                        EditInformationFragmentPhone.setText(clientGetProfile.getPhone());
                        getGaverment();
                        getBloodTypes();
                    } else {
                        EditInformationFragmentProgressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), getProfile.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetProfile> call, Throwable t) {
                EditInformationFragmentProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        EditInformationFragmentDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperMethod.showCalender(getActivity(), getString(R.string.date), EditInformationFragmentDateOfBirth, dateModel1);
            }
        });

        EditInformationFragmentLastDateDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HelperMethod.showCalender(getActivity(), getString(R.string.last_date), EditInformationFragmentLastDateDonation, dateModel2);
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getGaverment() {
        apiServices = getRetrofit().create(APIServices.class);
        final Call<Governorates> governorates = apiServices.getGovernorates();
        governorates.enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                if (response.body().getStatus() == 1) {
                    try {
                        int pio = 0;
                        Governorates governorates1 = response.body();
                        stringsGaverment.add(getString(R.string.select_gaverment));
                        IdsGeverment.add(0);
                        List<GovernoratesData> governoratesData = governorates1.getData();
                        for (int i = 0; i < governoratesData.size(); i++) {
                            getResult = governoratesData.get(i).getName();
                            stringsGaverment.add(getResult);
                            positionGaverment = governoratesData.get(i).getId();
                            IdsGeverment.add(positionGaverment);
                            if (clientGetProfile.getCity().getGovernorate().getId().equals(governoratesData.get(i).getId())) {
                                pio = i + 1;
                            }
                        }
                        HelperMethod.showGovernorates(stringsGaverment, getActivity(), EditInformationFragmentSPGaverment);
                        EditInformationFragmentSPGaverment.setSelection(pio);
                        EditInformationFragmentSPGaverment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position != 0) {
                                    getCities(IdsGeverment.get(position));
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

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
    }

    private void getCities(int getIdGovernorates) {
        APIServices apiServicesgetCities = getRetrofit().create(APIServices.class);
        final Call<Cities> citiesCall = apiServicesgetCities.getCities(getIdGovernorates);
        citiesCall.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                Cities cities = response.body();
                if (cities.getStatus() == 1) {
                    try {
                        String getResult;
                        ArrayList<String> stringsCity = new ArrayList<>();
                        int pio = 0;
                        IdsCity = new ArrayList<>();
                        stringsCity.add(getString(R.string.select_city));
                        IdsCity.add(0);
                        List<DataCities> dataCities = cities.getData();
                        for (int i = 0; i < dataCities.size(); i++) {
                            getResult = dataCities.get(i).getName();
                            stringsCity.add(getResult);
                            positionCity = dataCities.get(i).getId();
                            IdsCity.add(positionCity);
                            if (clientGetProfile.getCity().getId().equals(dataCities.get(i).getId())) {
                                pio = i + 1;
                            }
                        }
                        HelperMethod.showGovernorates(stringsCity, getActivity(), EditInformationFragmentSPCity);
                        EditInformationFragmentSPCity.setSelection(pio);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
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
                BloodTypes bloodTypes = response.body();
                if (bloodTypes.getStatus() == 1) {
                    try {
                        stringsBloodType.add(getString(R.string.blood_type));
                        IdsBloodType.add(0);
                        List<DataBloodTypes> bloodTypesList = bloodTypes.getData();
                        int pio = 0;
                        for (int i = 0; i < bloodTypesList.size(); i++) {
                            getResult = bloodTypesList.get(i).getName();
                            stringsBloodType.add(getResult);
                            positionBloodType = bloodTypesList.get(i).getId();
                            IdsBloodType.add(positionBloodType);
                            if (clientGetProfile.getBloodType().getId().equals(bloodTypesList.get(i).getId())) {
                                pio = i + 1;
                            }
                        }
                        HelperMethod.showGovernorates(stringsBloodType, getActivity(), EditInformationFragmentSPBloodType);

                        EditInformationFragmentSPBloodType.setSelection(pio);

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

    @OnClick(R.id.EditInformationFragment_BT_Regester)
    public void onViewClicked() {
        // for check network
        if (check_network == false) {
            return;
        }
        String new_user = EditInformationFragmentName.getText().toString().trim();
        String email = EditInformationFragmentEmail.getText().toString().trim();
        String date_birth = EditInformationFragmentDateOfBirth.getText().toString().trim();
        String last_date = EditInformationFragmentLastDateDonation.getText().toString().trim();
        String phone = EditInformationFragmentPhone.getText().toString().trim();
        String password = EditInformationFragmentPassword.getText().toString().trim();
        String retry_password = EditInformationFragmentRetryPassword.getText().toString().trim();
        if (new_user.isEmpty() || email.isEmpty() || date_birth.isEmpty() || last_date.isEmpty() || phone.isEmpty() || password.isEmpty() || retry_password.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.all_filed_request), Toast.LENGTH_SHORT).show();
            return;
        }
        int blood_type = IdsBloodType.get(EditInformationFragmentSPBloodType.getSelectedItemPosition());
        int cityId = IdsCity.get(EditInformationFragmentSPCity.getSelectedItemPosition());
        if (blood_type <= 0 || cityId <= 0) {
            Toast.makeText(getActivity(), getString(R.string.selct_blood_and_city), Toast.LENGTH_SHORT).show();
            return;
        }
        Call<EditProfile> profileCall = apiServices.editProfile(new_user, email,
                date_birth, cityId, phone,
                last_date, password, retry_password, blood_type,rememberMy.getAPIKey());
        profileCall.enqueue(new Callback<EditProfile>() {
            @Override
            public void onResponse(Call<EditProfile> call, Response<EditProfile> response) {
                try {
                    EditProfile profile = response.body();
                    if (profile.getStatus() == 0) {
                        Toast.makeText(getActivity(), profile.getMsg(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), profile.getMsg(), Toast.LENGTH_LONG).show();
                        HomeFragment homeFragment = new HomeFragment();
                        HelperMethod.replece(homeFragment, getActivity().getSupportFragmentManager(),
                                R.id.Cycle_Home_contener, toolbar, getString(R.string.home));
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<EditProfile> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setTitle(R.string.edit_information);
    }
}