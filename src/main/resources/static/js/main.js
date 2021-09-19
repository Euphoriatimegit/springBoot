const usersFetchUrl = "http://localhost:8080/rest/admin";
const oneUserFetchUrl = "http://localhost:8080/rest/user";

let form = $('#newUserForm');

async function updateTable(fetchUrl, usersTable) {
    await fetch(fetchUrl, {
        method: "GET",
        headers: {
            'Cache-Control': 'no-cache'
        }
    })
        .then(res => {
                res.json().then(data => {
                        console.log(data);

                        let users = [];

                        if ($.isArray(data)) {
                            users = data;
                        } else {
                            users.push(data);
                        }

                        $(usersTable).find('tr').remove();

                        for (let i = 0; i < users.length; i++) {
                            if (usersTable === "#userTable") {
                                newRow(users[i], false)
                            } else {
                                newRow(users[i], true)
                            }
                        }
                    }
                )
            }
        )
}

function newRow(user, isButtons) {
    let row = `<tr id="row${user.id}">
    <td>${user.id}</td>                                             
    <td>${user.name}</td>                                             
    <td>${user.age}</td>
    <td>${user.email}</td>
    <td>${user.login}</td>                                             
    <td >${user.roles.map(function (item) {
            return item.name.replaceAll('ROLE_', '');
        }
    ).join(' ')
    }
                                             </td>`;

    if (!isButtons) {
        row += `</tr>`;
        $('#userTable').append(row);
    } else {
        row += `<td>
        <button type="button" class="btn btn-info btn-sm" data-toggle="modal" 
        onclick="editUser(${user.id})">Edit</button>
        </td>
        <td>
        <button type="button" class="btn btn-danger btn-sm" data-toggle="modal"
        onclick="deleteUser(${user.id})">Delete</button>
        </td>
        </tr>`;
    }

    $('#usersTable').append(row);
}

function editUser(id) {
    $('#editModal').modal()
    fillRoles("#edit_roles")
    loadDataToFormData("#row" + id, "#edit")
}

async function fillRoles(select) {
    $(select).find('option').remove();

    await fetch("http://localhost:8080/rest/roles")
        .then(res => {
                res.json().then(roles => {
                        for (let i = 0; i < roles.length; i++) {
                            let option = new Option();
                            option.id = roles[i].id;
                            option.value = roles[i].name;
                            option.innerHTML = roles[i].name.replaceAll('ROLE_', '');
                            $(select).append(option);
                        }
                    }
                )
            }
        )
}

function loadDataToFormData(id, action) {
    $(id).each(async function () {
        $(action + '_id').val($(this).find("td:eq(0)").html())
        $(action + '_name').val($(this).find("td:eq(1)").html());
        $(action + '_age').val($(this).find("td:eq(2)").html());
        $(action + '_email').val($(this).find("td:eq(3)").html());
        $(action + '_login').val($(this).find("td:eq(4)").html());

        let userRole = $(this).find("td:eq(5)").html();

        await fetch("http://localhost:8080/rest/roles")
            .then(res => {
                    res.json().then(roles => {
                            for (let i = 0; i < roles.length; i++) {
                                if (userRole.includes(roles[i].name.replaceAll('ROLE_', ''))) {
                                    let selector = 'option[value= "' + roles[i].name + '"]';
                                    $(selector).prop('selected', true);
                                }
                            }
                        }
                    )
                }
            )
    });
}

function deleteUser(id) {
    $('#deleteModal').modal()
    fillRoles("#delete_roles")
    loadDataToFormData("#row" + id, "#delete")
}

async function createNewUser() {
    let data = getFormData('#newUserForm');

    await fetch(usersFetchUrl, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(data)
    }).then(res => res.json().then(user => {
        newRow(user, true);
    }));

    clearUserEditForm();
    $('#allUsersTab').trigger("click");
}

function getFormData(form) {

    let selectedRoles = [];
    $(form + ' option:selected').map(function () {
        selectedRoles.push({
            id: $(this).attr('id'),
            name: $(this).val()
        });
    });

    let user = {
        id: $(form + ' input[name="id"]').val(),
        name: $(form + ' input[name="name"]').val(),
        age: $(form + ' input[name="age"]').val(),
        email: $(form + ' input[name="email"]').val(),
        login: $(form + ' input[name="login"]').val(),
        password: $(form + ' input[name="password"]').val(),
        roles: selectedRoles
    }
    return user;
}

function clearUserEditForm() {
    form.find(":input").val("");
}

async function updateUser() {
    let data = getFormData('#editForm');
    let id = data.id;

    await fetch(usersFetchUrl + "/" + id, {
        method: "PATCH",
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(data)
    }).then(res => res.json());

    updateRow("#row" + id, data)

    $('#editModal').modal('toggle')

}

function updateRow(rowId, user) {
    let row = $(rowId).find('td')

    console.log(row);
    row.eq(1).html(user.name);
    row.eq(2).html(user.age);
    row.eq(3).html(user.email);
    row.eq(4).html(user.login);
    row.eq(5).html(user.roles.map(function (item) {
            return item.name.replaceAll('ROLE_', '');
        }
    ).join(' '));
}

async function removeUser() {
    let data = getFormData('#deleteForm');
    let id = data.id;

    await fetch(usersFetchUrl + "/" + id, {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
    }).then()

    deleteRow("#row" + id);
    $('#deleteModal').modal('toggle')

}

function deleteRow(rowId) {
    $(rowId).remove();
}

updateTable(oneUserFetchUrl, "#userTable").then()
updateTable(usersFetchUrl,'#usersTable')
fillRoles('#roles')