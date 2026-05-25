(function() {
    const path = window.location.pathname;
    const isPublicPage = path.endsWith('/login.html') || path.endsWith('/register.html');
    const userJson = localStorage.getItem("user");

    if (!userJson) {
        if (!isPublicPage) {
            window.location.href = "/login.html";
        }
    } else {
        const user = JSON.parse(userJson);
        if (user.role === 'ADMIN' && (path.endsWith('/index.html') || path.endsWith('/') || path.endsWith('/aspirasi.html') || path.endsWith('/riwayat.html') || path.endsWith('/profile.html') || path.endsWith('/forum.html') && path.indexOf('/admin.html') === -1)) {
            window.location.href = "/admin.html";
        } else if (user.role === 'MAHASISWA' && path.endsWith('/admin.html')) {
            window.location.href = "/index.html";
        }
    }
})();

async function authenticatedFetch(url, options = {}) {
    const userJson = localStorage.getItem("user");
    if (!userJson) {
        window.location.href = "/login.html";
        throw new Error("Sesi tidak valid");
    }

    const user = JSON.parse(userJson);
    options.headers = options.headers || {};
    options.headers["Authorization"] = `Bearer ${user.token}`;

    const res = await fetch(url, options);

    if (res.status === 401) {
        localStorage.removeItem("user");
        window.location.href = "/login.html";
        throw new Error("Sesi berakhir, silakan login kembali.");
    }

    return res;
}