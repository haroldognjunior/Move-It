//Dictionary
// Bug for Linux corrected
const i18n = {
  "en-EN": {
    common: {
      header: {
        home: "Home",
        about: "About",
        search: "Search",
        services: "Services",
        professional: "Professional Service",
        countrywide: "Countrywide",
        touch: "Personal Touch",
        move: "Move with Joy",
        p1: "Welcome to our website, where we are on a mission to provide exceptional moving services to customers in the US. As a startup, we believe that moving doesn't have to be stressful or complicated, and we are passionate about making the process as seamless and enjoyable as possible.",
        quote: "Get a Quote",
        contact: "Contact Us",
        pro: "Professional",
        p2: "Our team of professional movers are trained to prioritize efficiency, organization, and attention to detail. We understand that your possessions are more than just objects - they are a reflection of your life, memories, and experiences. That's why we take extra care to ensure that everything is packed and transported with the utmost care and attention.",
        why: "Why Move With Us",
        p3: "We offer a range of services to suit your individual needs, whether you're moving locally or across the country. Our team can handle everything from packing and loading to unloading and unpacking, so you can focus on settling into your new home. And with our transparent pricing and no hidden fees, you can trust that you're getting a fair and competitive rate for our services.",
        p4: "At our core, we believe that moving should be an exciting and positive experience, not a stressful one. By providing exceptional moving services in the US, we hope to revolutionize the way people think about moving and provide a better, more personalized experience for our customers. Contact us today to learn more about how we can help you with your next move.",
        s1: "Move It",
        s2: "Move It",
        s3: "Move It",
        faqs: "FAQs",
        currency: "Currency Converter",
        amount: "Amount",
        from: "From",
        to: "To",
        convert: "Convert",
        login: "Login",
        lang: {
          title: "Languages",
          english: "English",
          spanish: "Spanish",
        },
      },
    },
  },
  "es-ES": {
    common: {
      header: {
        home: "Inicio",
        about: "Nosotros",
        search: "Buscar",
        services: "Servicios",
        professional: "Servicio Profesional",
        countrywide: "A Todo el País",
        touch: "Toque Personal",
        move: "Múdese sin estrés",
        p1: "Bienvenido a nuestro sitio web. Nuestra misión es brindarle un servicio de mudanza excepcional a nuestros clientes de Argentina. Creemos que la mudanza no debe ser estresante o complicada y nos apasiona que el proceso resulte perfecto y agradable para nuestros clientes.",
        quote: "Pedir Presupuesto",
        contact: "Contáctenos",
        pro: "Profesionales",
        p2: "Nuestro equipo de mudadores profesionales están entrenados para priorizar la eficiencia, la organización y la atención a los detalles. Entendemos que sus pertenencias son más que objetos, son el reflejo de su vida, sus recuerdos y sus experiencias. Es por eso que ponemos especial cuidado para asegurar que todo sea embalado y transportado con el mayor cuidado y atención.",
        why: "¿Por qué Mudarse con Nosotros?",
        p3: "Ofrecemos una variedad de servicios que se ajustan a sus necesidades, ya sea que se mude localmente o a otra parte del país. Nuestro equipo se encarga de todo desde el embalaje, la carga y descarga hasta el desembalado, así usted se puede enfocar en acomodarse en su nuevo hogar. Y con nuestros precios transparentes y sin costos sorpresa, puede estar seguro de que las tarifas por nuestros servicios serán razonables y competitivas.",
        p4: "De verdad creemos que mudarse debería ser una experiencia emocionante y positiva. Al ofrecer un servicio de mudanza excepcional, esperamos revolucionar el concepto de mudanza al ofrecer un servicio de excelencia y personalizado a nuestros clientes. Contáctenos hoy mismo para saber como podemos ayudarlo en su mudanza.",
        s1: "Move It",
        s2: "Move It",
        s3: "Move It",
        faqs: "Preguntas Frecuentes",
        currency: "Conversor de Monedas",
        amount: "Monto",
        from: "De",
        to: "A",
        convert: "Convertir",
        login: "Iniciar Sesión",
        lang: {
          title: "Idiomas",
          english: "Ingles",
          spanish: "Español",
        },
      },
    },
  },
};

function translate(lang) {
  const elementsWithI18n = document.querySelectorAll("[data-i18n]");

  if (!i18n[lang]) {
    console.error("El objeto de idioma no está definido para:", lang);
    return;
  }

  elementsWithI18n.forEach(function (element) {
    const arrayKeys = element.attributes["data-i18n"].nodeValue.split(".");
    const traduccion = accederObjeto(i18n[lang], arrayKeys);
    element.innerText = traduccion;
  });
}

function accederObjeto(objeto, elementos, indice = 0) {
  if (indice === elementos.length) {
    return objeto;
  }

  const elemento = elementos[indice];
  if (objeto[elemento] === undefined) {
    console.error("No se encontró la clave", elemento, "en el objeto", objeto);
    return "[Texto no encontrado]";
  }

  return accederObjeto(objeto[elemento], elementos, indice + 1);
}

function onChangeLanguage(newLanguage) {
  lang = newLanguage;
  translate(lang);
}

let lang = navigator.language || "en-EN";
if (!i18n[lang]) {
  lang = lang.startsWith("es") ? "es-ES" : "en-EN"; // Ajuste por defecto basado en el prefijo del idioma
}

document.addEventListener("DOMContentLoaded", function () {
  init(); // Iniciar traducción después de que el DOM esté listo
});

function init() {
  translate(lang);
}
