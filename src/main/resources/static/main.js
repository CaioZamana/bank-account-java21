const API_BASE_URL = 'http://localhost:8080'; // Altere para o endereço correto do backend

// Criar Conta Poupança
if (document.getElementById('create-saving-account-form')) {
    document.getElementById('create-saving-account-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const accountNameHolder = document.getElementById('accountNameHolder').value.trim();

        if (!accountNameHolder) {
            alert("Account Name Holder is required.");
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/saving-account/create?accountNameHolder=${encodeURIComponent(accountNameHolder)}`, {
                method: 'POST',
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(`HTTP ${response.status}: ${error}`);
            }

            const result = await response.json();
            alert(`Saving Account Created: ID - ${result.id}, Name - ${result.accountHolder}, Account Number - ${result.accountNumber}`);
            window.location.href = "index.html"; // Redireciona de volta à página inicial
        } catch (error) {
            console.error("Error creating saving account:", error);
            alert("Error creating saving account: " + error.message);
        }
    });
}

// Transferência
if (document.getElementById('transfer-form')) {
    document.getElementById('transfer-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const fromAccount = document.getElementById('fromAccount').value.trim();
        const toAccount = document.getElementById('toAccount').value.trim();
        const amount = parseFloat(document.getElementById('amount').value);

        if (!fromAccount || !toAccount || isNaN(amount) || amount <= 0) {
            alert("All fields are required and amount must be greater than zero.");
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/account/operation/transfer?fromAccountNumber=${encodeURIComponent(fromAccount)}&toAccountNumber=${encodeURIComponent(toAccount)}&amount=${encodeURIComponent(amount)}`, {
                method: 'POST',
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(`HTTP ${response.status}: ${error}`);
            }

            const result = await response.text();
            alert(result);
        } catch (error) {
            console.error("Error transferring money:", error);
            alert("Error transferring money: " + error.message);
        }
    });
}

// Depósito
if (document.getElementById('deposit-form')) {
    document.getElementById('deposit-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const toAccount = document.getElementById('toAccount').value.trim();
        const amount = parseFloat(document.getElementById('amount').value);

        if (!toAccount || isNaN(amount) || amount <= 0) {
            alert("All fields are required and amount must be greater than zero.");
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/account/operation/deposit?toAccount=${encodeURIComponent(toAccount)}&amount=${encodeURIComponent(amount)}`, {
                method: 'POST',
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(`HTTP ${response.status}: ${error}`);
            }

            const result = await response.text();
            alert(result);
        } catch (error) {
            console.error("Error depositing money:", error);
            alert("Error depositing money: " + error.message);
        }
    });
}

// Saque
if (document.getElementById('withdraw-form')) {
    document.getElementById('withdraw-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const fromAccount = document.getElementById('fromAccount').value.trim();
        const amount = parseFloat(document.getElementById('amount').value);

        if (!fromAccount || isNaN(amount) || amount <= 0) {
            alert("All fields are required and amount must be greater than zero.");
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/account/operation/withdraw?fromAccount=${encodeURIComponent(fromAccount)}&amount=${encodeURIComponent(amount)}`, {
                method: 'POST',
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(`HTTP ${response.status}: ${error}`);
            }

            const result = await response.text();
            alert(result);
        } catch (error) {
            console.error("Error withdrawing money:", error);
            alert("Error withdrawing money: " + error.message);
        }
    });
}

// Consulta de Saldo
if (document.getElementById('balance-form')) {
    document.getElementById('balance-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const accountToBalance = document.getElementById('accountNumber').value.trim();

        if (!accountToBalance) {
            alert("Account number is required.");
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/account/operation/balance?accountToBalance=${encodeURIComponent(accountToBalance)}`, {
                method: 'GET',
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(`HTTP ${response.status}: ${error}`);
            }

            const result = await response.json();
            document.getElementById('balance-result').textContent = `Balance: ${result.balance}`;
        } catch (error) {
            console.error("Error fetching balance:", error);
            alert("Error fetching balance: " + error.message);
        }
    });
}

// Listagem de Todas as Contas
if (document.getElementById('get-accounts')) {
    document.getElementById('get-accounts').addEventListener('click', async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/account/operation/get-all-accounts`, {
                method: 'GET',
            });

            if (!response.ok) {
                const error = await response.text();
                throw new Error(`HTTP ${response.status}: ${error}`);
            }

            const data = await response.json();
            const listElement = document.getElementById('accounts-list');
            listElement.innerHTML = ''; // Limpar lista anterior

            if (data.content && data.content.length > 0) {
                data.content.forEach(account => {
                    const listItem = document.createElement('li');
                    listItem.textContent = `ID: ${account.id}, Name: ${account.accountHolder}, Account Number: ${account.accountNumber}, Balance: ${account.balance}`;
                    listElement.appendChild(listItem);
                });
            } else {
                alert('No accounts found!');
            }
        } catch (error) {
            console.error("Error fetching accounts:", error);
            alert("Error fetching accounts: " + error.message);
        }
    });
}
