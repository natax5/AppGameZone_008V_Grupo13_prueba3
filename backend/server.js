const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const PORT = 3000;

app.use(cors());
app.use(bodyParser.json());

// Base de datos en memoria (simulada para el ejemplo)
let users = [];

// Endpoint de Registro (CREATE)
app.post('/register', (req, res) => {
    const { fullName, email, password, phone, genres } = req.body;

    if (!fullName || !email || !password) {
        return res.status(400).json({ message: 'Faltan datos obligatorios' });
    }

    const existingUser = users.find(u => u.email === email);
    if (existingUser) {
        return res.status(409).json({ message: 'El usuario ya existe' });
    }

    const newUser = { id: users.length + 1, fullName, email, password, phone, genres };
    users.push(newUser);

    console.log(`Usuario registrado: ${email}`);
    res.status(201).json({ message: 'Usuario registrado con éxito', userId: newUser.id });
});

// Endpoint de Login (READ)
app.post('/login', (req, res) => {
    const { email, password } = req.body;

    const user = users.find(u => u.email === email && u.password === password);

    if (user) {
        console.log(`Login exitoso: ${email}`);
        res.status(200).json({ 
            message: 'Login exitoso', 
            user: { 
                id: user.id, 
                fullName: user.fullName, 
                email: user.email,
                phone: user.phone,
                genres: user.genres
            } 
        });
    } else {
        res.status(401).json({ message: 'Credenciales inválidas' });
    }
});

// Endpoint de Actualización (UPDATE)
app.put('/users/:id', (req, res) => {
    const userId = parseInt(req.params.id);
    const { fullName, email, phone, genres } = req.body;

    const userIndex = users.findIndex(u => u.id === userId);

    if (userIndex !== -1) {
        // Actualizamos los campos
        users[userIndex] = { ...users[userIndex], fullName, email, phone, genres };
        console.log(`Usuario actualizado: ${userId}`);
        res.status(200).json({ message: 'Usuario actualizado correctamente', user: users[userIndex] });
    } else {
        res.status(404).json({ message: 'Usuario no encontrado' });
    }
});

// Endpoint de Eliminación (DELETE)
app.delete('/users/:id', (req, res) => {
    const userId = parseInt(req.params.id);
    const initialLength = users.length;
    
    users = users.filter(u => u.id !== userId);

    if (users.length < initialLength) {
        console.log(`Usuario eliminado: ${userId}`);
        res.status(200).json({ message: 'Usuario eliminado correctamente' });
    } else {
        res.status(404).json({ message: 'Usuario no encontrado' });
    }
});

// Endpoint de Prueba
app.get('/', (req, res) => {
    res.send('GameZone API Microservice is running...');
});

// Escuchar en 0.0.0.0 para asegurar acceso desde red externa (Emulador)
app.listen(PORT, '0.0.0.0', () => {
    console.log(`Servidor corriendo en http://localhost:${PORT}`);
    console.log(`Esperando conexiones del emulador...`);
});