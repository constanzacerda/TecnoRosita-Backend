package tecnorosita.backend.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tecnorosita.backend.model.Categoria;
import tecnorosita.backend.model.Producto;
import tecnorosita.backend.model.Usuario;
import tecnorosita.backend.repository.CategoriaRepository;
import tecnorosita.backend.repository.ProductoRepository;
import tecnorosita.backend.repository.UsuarioRepository;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(CategoriaRepository categoriaRepository,
                          ProductoRepository productoRepository,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {


        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario(null, "admin@tecnorosita.cl", passwordEncoder.encode("admin123"), "Juan Carlos", "ROLE_ADMIN");
            Usuario vendedor = new Usuario(null, "vendedor@tecnorosita.cl", passwordEncoder.encode("vende123"), "Constanza Cerda", "ROLE_VENDEDOR");
            Usuario cliente = new Usuario(null, "cliente@correo.cl", passwordEncoder.encode("cliente123"), "Cliente Frecuente", "ROLE_CLIENTE");

            usuarioRepository.saveAll(List.of(admin, vendedor, cliente));
            System.out.println("✅ Seeder: Usuarios creados correctamente.");
        }


        if (categoriaRepository.count() == 0) {


            Categoria catTeclados = new Categoria(null, "Teclados");
            Categoria catMouse = new Categoria(null, "Mouse");
            Categoria catAudifonos = new Categoria(null, "Audífonos");
            Categoria catMonitores = new Categoria(null, "Monitores");
            Categoria catSillas = new Categoria(null, "Sillas");
            Categoria catLaptops = new Categoria(null, "Laptops");
            Categoria catAudio = new Categoria(null, "Audio");
            Categoria catComponentes = new Categoria(null, "Componentes de PC");
            Categoria catHogar = new Categoria(null, "Hogar Inteligente");
            Categoria catStreaming = new Categoria(null, "Streaming");
            Categoria catAccesorios = new Categoria(null, "Accesorios");

            categoriaRepository.saveAll(List.of(catTeclados, catMouse, catAudifonos, catMonitores, catSillas, catLaptops, catAudio, catComponentes, catHogar, catStreaming, catAccesorios));


            Producto p1 = new Producto(null, "Teclado Mecánico Razer Huntsman Mini, Switches Ópticos, 60%", 121990, "https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=600&q=80", catTeclados);
            Producto p2 = new Producto(null, "Teclado Logitech G Pro X TKL, Inalámbrico", 199990, "https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?auto=format&fit=crop&w=600&q=80", catTeclados);
            Producto p3 = new Producto(null, "Teclado Mecánico Corsair K100 RGB, Cherry MX", 249990, "https://images.unsplash.com/photo-1595225476474-87563907a212?auto=format&fit=crop&w=600&q=80", catTeclados);
            Producto p4 = new Producto(null, "Teclado SteelSeries Apex Pro TKL", 219990, "https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?auto=format&fit=crop&w=600&q=80", catTeclados);

            Producto p5 = new Producto(null, "Mouse Gamer Logitech G502 X Plus, Inalámbrico", 147990, "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?auto=format&fit=crop&w=600&q=80", catMouse);
            Producto p6 = new Producto(null, "Mouse Razer DeathAdder V3 Pro, Ultra Ligero", 152990, "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?auto=format&fit=crop&w=600&q=80", catMouse);
            Producto p7 = new Producto(null, "Mouse Glorious Model O Wireless, Matte", 84990, "https://images.unsplash.com/photo-1593111774616-a90e39da544d?auto=format&fit=crop&w=600&q=80", catMouse);
            Producto p8 = new Producto(null, "Mouse SteelSeries Aerox 3 Wireless", 97990, "https://images.unsplash.com/photo-1613141692099-1374409ef665?auto=format&fit=crop&w=600&q=80", catMouse);

            Producto p9 = new Producto(null, "Audífonos HyperX Cloud II Wireless", 136990, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=600&q=80", catAudifonos);
            Producto p10 = new Producto(null, "Audífonos Logitech G Pro X Wireless", 209990, "https://images.unsplash.com/photo-1484704849700-f032a568e944?auto=format&fit=crop&w=600&q=80", catAudifonos);
            Producto p11 = new Producto(null, "Audífonos Razer BlackShark V2 Pro", 172990, "https://images.unsplash.com/photo-1546435770-a3e426bf472b?auto=format&fit=crop&w=600&q=80", catAudifonos);
            Producto p12 = new Producto(null, "Audífonos SteelSeries Arctis Nova Pro", 349990, "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?auto=format&fit=crop&w=600&q=80", catAudifonos);

            Producto p13 = new Producto(null, "Monitor Gamer OLED MSI MAG 27\" 240Hz", 595640, "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?auto=format&fit=crop&w=600&q=80", catMonitores);
            Producto p14 = new Producto(null, "Monitor Samsung Odyssey G7 Curvo 32\"", 524990, "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=600&q=80", catMonitores);
            Producto p15 = new Producto(null, "Monitor ASUS ROG Swift 27\" QHD 240Hz", 684990, "https://images.unsplash.com/photo-1583863788434-e58a36330cf0?auto=format&fit=crop&w=600&q=80", catMonitores);
            Producto p16 = new Producto(null, "Monitor Alienware 34\" Ultrawide OLED", 949990, "https://images.unsplash.com/photo-1535223289827-42f1e9919769?auto=format&fit=crop&w=600&q=80", catMonitores);

            Producto p17 = new Producto(null, "Silla Gamer Secretlab TITAN Evo 2022", 579990, "https://images.unsplash.com/photo-1598550476439-6847785fcea6?auto=format&fit=crop&w=600&q=80", catSillas);
            Producto p18 = new Producto(null, "Silla Gamer Corsair TC200, Piel", 339990, "https://images.unsplash.com/photo-1580481077494-e3299ac25e94?auto=format&fit=crop&w=600&q=80", catSillas);
            Producto p19 = new Producto(null, "Silla Gamer Razer Iskur, Soporte Lumbar", 449990, "https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?auto=format&fit=crop&w=600&q=80", catSillas);
            Producto p20 = new Producto(null, "Silla Noblechairs Hero Series Premium", 519990, "https://images.unsplash.com/photo-1567538096630-e0c55bd6374c?auto=format&fit=crop&w=600&q=80", catSillas);

            Producto p21 = new Producto(null, "Laptop ASUS ROG Zephyrus G14, RTX 4080", 2299990, "https://images.unsplash.com/photo-1603302576837-37561b2e2302?auto=format&fit=crop&w=600&q=80", catLaptops);
            Producto p22 = new Producto(null, "Laptop Alienware m16 R1, RTX 4070", 1949990, "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?auto=format&fit=crop&w=600&q=80", catLaptops);
            Producto p23 = new Producto(null, "Notebook Lenovo Legion Pro 5i, RTX 4060", 1429990, "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?auto=format&fit=crop&w=600&q=80", catLaptops);
            Producto p24 = new Producto(null, "Laptop Razer Blade 15, RTX 4060", 2099990, "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?auto=format&fit=crop&w=600&q=80", catLaptops);

            Producto p25 = new Producto(null, "Monitores Activos Edifier R1280T", 115990, "https://images.unsplash.com/photo-1545454675-3531b543be5d?auto=format&fit=crop&w=600&q=80", catAudio);
            Producto p26 = new Producto(null, "Parlantes Gamer Logitech G560 RGB", 229990, "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?auto=format&fit=crop&w=600&q=80", catAudio);
            Producto p27 = new Producto(null, "Parlantes Creative Pebble V3, USB-C", 47990, "https://images.unsplash.com/photo-1543512214-318c7553f230?auto=format&fit=crop&w=600&q=80", catAudio);
            Producto p28 = new Producto(null, "Parlantes Razer Nommo V2 Pro", 449990, "https://images.unsplash.com/photo-1558089687-f282ffcbc126?auto=format&fit=crop&w=600&q=80", catAudio);

            Producto p29 = new Producto(null, "Tarjeta de Video ASUS ROG RTX 4090", 2299990, "https://images.unsplash.com/photo-1587202372775-e229f172b9d7?auto=format&fit=crop&w=600&q=80", catComponentes);
            Producto p30 = new Producto(null, "Procesador AMD Ryzen 7 7800X3D", 469990, "https://images.unsplash.com/photo-1591799264318-7e6ef8ddb7ea?auto=format&fit=crop&w=600&q=80", catComponentes);
            Producto p31 = new Producto(null, "Procesador Intel Core i9-14900K", 629990, "https://images.unsplash.com/photo-1555680202-c86f0e12f086?auto=format&fit=crop&w=600&q=80", catComponentes);
            Producto p32 = new Producto(null, "Placa Madre MSI MPG B650 Carbon WIFI", 309990, "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&w=600&q=80", catComponentes);

            Producto p33 = new Producto(null, "Amazon Echo Dot (5.ª Gen), Alexa", 52990, "https://images.unsplash.com/photo-1543512214-318c7553f230?auto=format&fit=crop&w=600&q=80", catHogar);
            Producto p34 = new Producto(null, "Google Nest Hub (2.ª Gen)", 94990, "https://images.unsplash.com/photo-1558089687-f282ffcbc126?auto=format&fit=crop&w=600&q=80", catHogar);
            Producto p35 = new Producto(null, "Ampolleta Inteligente Philips Hue", 57990, "https://images.unsplash.com/photo-1550985616-11b30d314f17?auto=format&fit=crop&w=600&q=80", catHogar);
            Producto p36 = new Producto(null, "Enchufe Inteligente TP-Link Tapo", 14990, "https://images.unsplash.com/photo-1513506003901-1e6a229e2d15?auto=format&fit=crop&w=600&q=80", catHogar);

            Producto p37 = new Producto(null, "Micrófono HyperX QuadCast S, RGB", 157990, "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?auto=format&fit=crop&w=600&q=80", catStreaming);
            Producto p38 = new Producto(null, "Elgato Stream Deck MK.2, 15 Teclas", 152990, "https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=600&q=80", catStreaming);
            Producto p39 = new Producto(null, "Cámara Web Logitech C920 HD Pro", 79990, "https://images.unsplash.com/photo-1587831990711-23ca6441447b?auto=format&fit=crop&w=600&q=80", catStreaming);
            Producto p40 = new Producto(null, "Capturadora Elgato HD60 X, 4K30", 199990, "https://images.unsplash.com/photo-1612287230202-1ff1d85d1bdf?auto=format&fit=crop&w=600&q=80", catStreaming);

            Producto p41 = new Producto(null, "Mousepad Razer Goliathus Chroma", 57990, "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?auto=format&fit=crop&w=600&q=80", catAccesorios);
            Producto p42 = new Producto(null, "Soporte para Audífonos Corsair ST100", 74990, "https://images.unsplash.com/photo-1583394838336-acd977736f90?auto=format&fit=crop&w=600&q=80", catAccesorios);
            Producto p43 = new Producto(null, "Hub USB-C Anker 7-en-1, 4K HDMI", 47990, "https://images.unsplash.com/photo-1544652478-6653e09f18a2?auto=format&fit=crop&w=600&q=80", catAccesorios);
            Producto p44 = new Producto(null, "Bungee para Mouse Zowie Camade II", 26990, "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?auto=format&fit=crop&w=600&q=80", catAccesorios);

            productoRepository.saveAll(List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18, p19, p20, p21, p22, p23, p24, p25, p26, p27, p28, p29, p30, p31, p32, p33, p34, p35, p36, p37, p38, p39, p40, p41, p42, p43, p44));

            System.out.println("✅ Seeder: 44 Productos y 11 Categorías insertados con éxito.");
        }
    }
}
