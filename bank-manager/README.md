# Bank Manager Application - UI Extension Guide

Guide on how to add new buttons or functionalities.

---

## 1. Declare the control in `ActionsPanel`

- Create the button (or other control) and add it to the panel.
- Expose a **getter** so external classes can register listeners.

```java
// ActionsPanel.java (example)
private final JButton freezeAccountButton = new JButton("Freeze Account"); // 1. declare

public ActionsPanel() {
    super(new FlowLayout(FlowLayout.RIGHT));
    // ...
    add(freezeAccountButton); // 2. add to layout
}

public JButton getFreezeAccountButton() { // 3. expose getter
    return freezeAccountButton;
}
```

---

## 2. Handle the event in the main frame (`BankManagerApp`)

- In your main window, **wire** the listener.
- Keep UI reading/writing here; push business rules to services/dao/models.

```java
// BankManagerApp.java (example)
actionsPanel.getFreezeAccountButton().

addActionListener(e ->

onFreezeAccount());

private void onFreezeAccount() {
    CheckingAccount selected = accountsTablePanel.getSelectedAccount();
    if (selected == null) {
        JOptionPane.showMessageDialog(this, "Select an account first.");
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(this,
            "Freeze account #" + selected.getAccountNumber() + "?",
            "Confirm", JOptionPane.OK_CANCEL_OPTION);
    if (confirm != JOptionPane.OK_OPTION) return;

    // 2. call service/dao
    // accountDAO.freeze(selected.getAccountNumber());

    JOptionPane.showMessageDialog(this, "Account frozen (demo).");
}
```

---

## 3. (Optional) Add DAO/service logic

If the feature needs persistence:

- Add a method to `AccountDAO` (e.g., `freeze(int number)` or `updateStatus(...)`).
- Add columns if needed (e.g., `status ENUM('ACTIVE','FROZEN')`).

---

## Extension checklist

- UI control → `ActionsPanel`
- Table or details → `AccountsTablePanel`, `AccountDetailsPanel`
- Event wiring → `BankManagerApp`
- Persistence → `AccountDAO`
- Domain changes → `model`
- Business rules → `service`
