# TxAdmin Client (mod Fabric, Minecraft 26.3)

Mod klien untuk tampilan menu txAdmin. Bekerja bersama plugin TxAdmin di server (channel `txadmin:main`).
Semua aksi tetap diperiksa di server (harus admin di config plugin).

## Build
1. Buat repo GitHub baru, upload seluruh isi folder ini.
2. Tab Actions -> workflow "Build mod" jalan otomatis.
3. Download artifact `txadmin-client`, ambil jar tanpa `-sources`.

## Pasang (tiap admin)
1. Fabric Loader untuk 26.3 + Fabric API untuk 26.3 di folder mods.
2. Taruh `txadmin-client-1.0.0.jar` di folder mods.
3. Options > Controls > Key Binds > kategori **txAdmin**: pasang tombol APA SAJA (Home, 0, ', F1, huruf, dll).
