package org.projectapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.projectapp.Adapter.ViewAttendenceAdapter;
import org.projectapp.Model.Document;
import org.projectapp.Model.Student;
import org.projectapp.Model.Subject;

public class ViewTheoryAttendenceActivity extends AppCompatActivity {
    ArrayList<Integer> absent;
    RecyclerView.Adapter adapter;
    String department = null;
    Boolean flag = false;
    LinearLayoutManager layoutManager;
    ArrayList<Integer> present;
    String[] semArray;
    String semester = null;
    RecyclerView student_list;
    AppCompatSpinner student_semester;
    AppCompatSpinner student_subject;
    String subject = null;
    ArrayList<String> subjects;
    AppCompatTextView title;
    ArrayList<Integer> total;
    List<Student> uploads;
    String year = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_theory_attendence);
        this.department = getIntent().getStringExtra("department");
        this.title = (AppCompatTextView) findViewById(R.id.title);
        this.title.setText("Theory Attendence");
        Calendar instance = Calendar.getInstance();
        if (instance.get(2) < instance.get(5)) {
            this.year = (instance.get(1) - 1) + "-" + instance.get(1);
        } else if (instance.get(2) == 11) {
            this.year = instance.get(1) + "-" + (instance.get(1) + 1);
        } else {
            this.year = instance.get(1) + "-" + (instance.get(1) + 1);
        }
        this.student_semester = (AppCompatSpinner) findViewById(R.id.student_semester);
        this.student_subject = (AppCompatSpinner) findViewById(R.id.student_subject);
        this.subjects = new ArrayList<>();
        this.present = new ArrayList<>();
        this.absent = new ArrayList<>();
        this.total = new ArrayList<>();
        this.subjects.add("Select Subject");
        this.layoutManager = new LinearLayoutManager(this);
        this.student_list = (RecyclerView) findViewById(R.id.student_list);
        this.student_list.setHasFixedSize(true);
        this.student_list.setLayoutManager(this.layoutManager);
        this.uploads = new ArrayList();
        if (this.department.equals("Computer Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CO2I", "CO4I", "CO6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CO1I", "CO3I", "CO5I"};
            }
        } else if (this.department.equals("IT Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "IF2I", "IF4I", "IF6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "IF1I", "IF3I", "IF5I"};
            }
        } else if (this.department.equals("Civil I Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CE2I", "CE4I", "CE6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CE1I", "CE3I", "CE5I"};
            }
        } else if (this.department.equals("Civil II Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CE2I", "CE4I", "CE6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CE1I", "CE3I", "CE5I"};
            }
        } else if (this.department.equals("Mechanical I Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "ME2I", "ME4I", "ME6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "ME1I", "ME3I", "ME5I"};
            }
        } else if (this.department.equals("Mechanical II Shift Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "ME2I", "ME4I", "ME6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "ME1I", "ME3I", "ME5I"};
            }
        } else if (this.department.equals("Chemical Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "CH2I", "CH4I", "CH6I"};
            } else {
                this.semArray = new String[]{"Select Semester", "CH1I", "CH3I", "CH5I"};
            }
        } else if (this.department.equals("TR Department")) {
            if (instance.get(2) < 5 || instance.get(2) == 11) {
                this.semArray = new String[]{"Select Semester", "TR2G", "TR4G"};
            } else {
                this.semArray = new String[]{"Select Semester", "TR1G", "TR3G", "TR5G"};
            }
        }
        this.student_semester.setAdapter((SpinnerAdapter) new ArrayAdapter(this, R.layout.spinner_dialog, this.semArray));
        this.student_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ViewTheoryAttendenceActivity.this.uploads.clear();
                if (adapterView.getItemAtPosition(i).toString().equals("Select Semester")) {
                    ViewTheoryAttendenceActivity.this.semester = null;
                    return;
                }
                ViewTheoryAttendenceActivity.this.semester = adapterView.getItemAtPosition(i).toString();
                FirebaseDatabase instance = FirebaseDatabase.getInstance();
                instance.getReference(ViewTheoryAttendenceActivity.this.department + "/subjects/" + ViewTheoryAttendenceActivity.this.semester).addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                            Subject subject = (Subject) value.getValue(Subject.class);
                            if (subject.getName().contains("TH")) {
                                ViewTheoryAttendenceActivity.this.subjects.add(subject.getName());
                            }
                        }
                        ViewTheoryAttendenceActivity.this.student_subject.setAdapter((SpinnerAdapter) new ArrayAdapter(ViewTheoryAttendenceActivity.this, R.layout.spinner_dialog, ViewTheoryAttendenceActivity.this.subjects));
                    }
                });
            }
        });
        this.student_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ViewTheoryAttendenceActivity.this.uploads.clear();
                if (adapterView.getItemAtPosition(i).toString().equals("Select Subject")) {
                    ViewTheoryAttendenceActivity.this.subject = null;
                    return;
                }
                ViewTheoryAttendenceActivity.this.subject = adapterView.getItemAtPosition(i).toString();
                ViewTheoryAttendenceActivity viewTheoryAttendenceActivity = ViewTheoryAttendenceActivity.this;
                viewTheoryAttendenceActivity.viewAttendence(viewTheoryAttendenceActivity.semester, ViewTheoryAttendenceActivity.this.subject, ViewTheoryAttendenceActivity.this.year, ViewTheoryAttendenceActivity.this.department);
            }
        });
    }

    /* access modifiers changed from: private */
    public void viewAttendence(String str, String str2, String str3, String str4) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Students Attendence...");
        progressDialog.show();
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        final String str5 = str;
        final String str6 = str3;
        final String str7 = str2;
        final String str8 = str4;
        instance.getReference(str4 + "/Theory Attendence/" + str + " " + str3).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewTheoryAttendenceActivity.this);
                    builder.setTitle((CharSequence) "Alert");
                    builder.setMessage((CharSequence) "Attendence Sheet Not Found.");
                    builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ViewTheoryAttendenceActivity.this.onBackPressed();
                        }
                    });
                    builder.create();
                    if (ViewTheoryAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                        builder.show();
                        return;
                    }
                    return;
                }
                final Document document = (Document) dataSnapshot.getValue(Document.class);
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/rait/Document");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    final File file2 = new File(Environment.getExternalStorageDirectory().getPath() + "/rait/Document", str5 + " Theory Attendence " + str6 + ".xls");
                    file2.createNewFile();
                    FirebaseStorage.getInstance().getReferenceFromUrl(document.getUrl()).getFile(file2).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            ViewTheoryAttendenceActivity.this.uploads.clear();
                            try {
                                HSSFSheet sheet = new HSSFWorkbook((InputStream) new FileInputStream(file2)).getSheet(str7);
                                if (sheet != null) {
                                    if (sheet.getPhysicalNumberOfRows() != 0) {
                                        int i = 2;
                                        int i2 = 2;
                                        while (i2 <= sheet.getLastRowNum()) {
                                            HSSFRow row = sheet.getRow(i2);
                                            if (!(row == null || row.getCell(0) == null || row.getCell(1) == null || row.getCell(i) == null)) {
                                                String format = String.format("%.0f", new Object[]{Double.valueOf(row.getCell(0).getNumericCellValue())});
                                                String format2 = String.format("%.0f", new Object[]{Double.valueOf(row.getCell(1).getNumericCellValue())});
                                                String hSSFCell = row.getCell(i).toString();
                                                Student student = new Student();
                                                student.setName(hSSFCell);
                                                student.setRoll_no(format);
                                                student.setEnrollment(format2);
                                                int i3 = 0;
                                                int i4 = 0;
                                                int i5 = 0;
                                                for (int i6 = 3; i6 < row.getLastCellNum(); i6++) {
                                                    if (!row.getCell(i6).equals("")) {
                                                        if (row.getCell(i6).toString().equals("Present")) {
                                                            i3++;
                                                        } else if (row.getCell(i6).toString().equals("Absent")) {
                                                            i4++;
                                                        }
                                                        i5++;
                                                        ViewTheoryAttendenceActivity.this.flag = true;
                                                    }
                                                }
                                                if (!format.equals("") && !hSSFCell.equals("") && !format2.equals("")) {
                                                    ViewTheoryAttendenceActivity.this.uploads.add(student);
                                                    if (ViewTheoryAttendenceActivity.this.flag.booleanValue()) {
                                                        ViewTheoryAttendenceActivity.this.flag = false;
                                                        ViewTheoryAttendenceActivity.this.total.add(Integer.valueOf(i5));
                                                        ViewTheoryAttendenceActivity.this.present.add(Integer.valueOf(i3));
                                                        ViewTheoryAttendenceActivity.this.absent.add(Integer.valueOf(i4));
                                                    }
                                                }
                                            }
                                            i2++;
                                            i = 2;
                                        }
                                        progressDialog.dismiss();
                                        if (ViewTheoryAttendenceActivity.this.total.size() == 0) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ViewTheoryAttendenceActivity.this);
                                            builder.setTitle((CharSequence) "Alert");
                                            builder.setMessage((CharSequence) "No Attendence available");
                                            builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ViewTheoryAttendenceActivity.this.onBackPressed();
                                                }
                                            });
                                            builder.create();
                                            if (ViewTheoryAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                                builder.show();
                                                return;
                                            }
                                            return;
                                        } else if (ViewTheoryAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.INITIALIZED)) {
                                            ViewTheoryAttendenceActivity.this.adapter = new ViewAttendenceAdapter(ViewTheoryAttendenceActivity.this, document.getUrl(), str5 + " Theory Attendence " + str6, ViewTheoryAttendenceActivity.this.uploads, ViewTheoryAttendenceActivity.this.total, ViewTheoryAttendenceActivity.this.present, ViewTheoryAttendenceActivity.this.absent, str5, str8, str7);
                                            ViewTheoryAttendenceActivity.this.student_list.setAdapter(ViewTheoryAttendenceActivity.this.adapter);
                                            return;
                                        } else {
                                            return;
                                        }
                                    }
                                }
                                progressDialog.dismiss();
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(ViewTheoryAttendenceActivity.this);
                                builder2.setTitle((CharSequence) "Alert");
                                builder2.setMessage((CharSequence) "Attendance of Current Subject Not Found.\nPossible Reasons are:\n1. New Subjects Added and Attendance Sheet Not Updated.\nContact H.O.D");
                                builder2.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) null);
                                builder2.create();
                                if (ViewTheoryAttendenceActivity.this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.CREATED)) {
                                    builder2.show();
                                }
                            } catch (IOException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener((OnFailureListener) new OnFailureListener() {
                        public void onFailure(@NonNull Exception exc) {
                            progressDialog.dismiss();
                            Toast.makeText(ViewTheoryAttendenceActivity.this, "F", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
