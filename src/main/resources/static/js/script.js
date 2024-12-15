function profileEdit() {
  document.getElementById('customerName').readOnly = false;
  document.getElementById('email').readOnly = false;
  document.getElementById('address').readOnly = false;
  document.getElementById('phone').readOnly = false;
  console.log("Thay đổi trạng thái thành công");
};

$("input[type=checkbox]").click(function checkBoxClick(){
                                    console.log("Adding value");
                                    var checkboxes = document.getElementsByName(myCheckBox);
                                    var checkboxesChecked = "";
                                    for (var i=0; i<checkboxes.length; i++) {
                                     if (checkboxes[i].checked) {
                                        checkboxesChecked = checkboxesChecked + checkboxes[i].getAttribute("data-id").toString() + ",";
                                     }
                                     document.getElementById("workTimeList").value = checkboxesChecked;
                                    });


