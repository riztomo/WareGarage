# WareGarage
Ini adalah repositori dari aplikasi WareGarage, sebuah aplikasi yang memudahkan Anda dalam melakukan manajemen terhadap gudang penyimpanan.


## Cara kerja aplikasi
Aplikasi ini adalah aplikasi manajemen gudang penyimpanan. Sebuah perusahaan dapat membuat beberapa tempat penyimpanan. Tempat penyimpanan tersebut memiliki kapasitas tertentu dan dapat dibagi menjadi beberapa section yang tiap sectionnya menyimpan barang tertentu. Jika barang tidak terlalu banyak dan masih ada ruang di dalam gudang, alokasi untuk sebuah jenis barang bisa ditambah dan dikurang. Barang dapat disimpan dan ditarik di mana tiap barang memiliki ID yang menyesuaikan jenisnya dan tanggal masuk. Tiap gudang memiliki sebuah akun pengurus dengan sebuah akun utama yang bisa membuat perusahaan dan mengakses semua gudang (OWNER dan MANAGER). Perusahaan juga memiliki data keluar masuk barang yang ditandai dengan tanggal masuk, tanggal keluar, dan kapasitas yang keluar.

Untuk menjalankan database gudang dan konten-kontennya, pengembang menggunakan [`PostgreSQL`](https://www.postgresql.org/) yang berjalan di server cloud milik [`Neon`](https://console.neon.tech/). Database ini terhubung ke backend berupa [`Node.js`](https://nodejs.org/en/) yang terhubung ke server PostgreSQL menggunakan [`pg`](https://www.npmjs.com/package/pg). Backend ini menjadi server untuk frontend berupa aplikasi Android yang dikembangkan dengan [`Android Studio`](https://developer.android.com/studio).

## Tabel SQL

*0. Enum*

Enum yang dibuat adalah commodities untuk tipe komoditas dan employee_type untuk role pegawai.

<pre>
create type employee_type as enum ('MANAGER', 'OWNER');
create type commodities as enum ('LIVE', 'FOOD', 'EARTH', 'ELECTRONIC', 'PHARMA', 'FURNITURE', 'TRANSPORT');
</pre>

*1. Employee*

Tabel Employee berisi pegawai, role-nya bisa berupa OWNER atau MANAGER.
<pre>
id bigserial primary key
username text unique not null
password text not null
name text not null
company_id bigint not null
role employee_type not null
</pre>

*2. Company*

Tabel Employee berisi database perusahaan yang bergabung.
<pre>
id bigserial primary key
name text unique not null
</pre>

*3. Warehouse*

Tabel Warehouse berisi warehouse milik perusahaan.
<pre>
id bigserial primary key
address text not null
name text not null
company_id bigint not null
manager_id bigint
rem_capacity bigint not null
</pre>

*4. Inout_records*

Tabel Inout_records berisi rekaman masuk-keluar komoditas.
<pre>
id bigserial primary key
company_id bigint not null
warehouse text not null
section text not null
commodity_id text not null
name text not null
type commodities not null
volume bigint not null
in_time date
out_time date
</pre>

*5. Section*

Tabel Section menunjukkan pembagian gudang-gudang berupa section.
<pre>
id bigserial primary key
warehouse_id bigint not null
name text not null
commodity_type commodities not null
rem_capacity bigint not null
</pre>

*6. Commodity*

Tabel Commodity menunjukkan komoditas yang disimpan dalam section.
<pre>
id bigserial primary key
commodity_id text unique
section_id bigint not null
volume bigint not null
name text not null
</pre>

## Relational diagrams

Entity-relationship diagram (ERD)

![ERD](https://github.com/riztomo/WareGarage/assets/91055987/f4c6be14-1abd-45f4-b08f-a11a784bdadf)

Unified modeling language (UML)

![SQL](https://github.com/riztomo/WareGarage/assets/91055987/6e48e010-139b-4845-888e-498c72926c97)

Flowchart

![Flowchart](https://github.com/riztomo/WareGarage/assets/91055987/b7045d3e-971b-493d-adaf-668650578fcb)
