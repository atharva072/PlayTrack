// import { jwtDecode } from "jwt-decode";
import { API_BASE } from "./config.js";

document.addEventListener("DOMContentLoaded", () => {
  
  document.getElementById("tagline").textContent = returnQuote();
  const form = document.getElementById("loginForm");
  const errorEl = document.getElementById("error");
  const registerLink = document.getElementById("registerLink");
  
  const registerKeyWord = document.getElementById("registerKeyWord");
  registerKeyWord.addEventListener("click", function (event) {
    event.preventDefault();

    const container1 = document.getElementById("container1");
    const container2 = document.getElementById("container2");
    container1.classList.add("slide-left");

    container2.classList.remove("offscreen-right");
    container2.classList.add("slide-in");
  });

  // logic to move back and forth
  document.getElementById("backToLogin").addEventListener("click", function (event) {
    event.preventDefault();

    const container1 = document.getElementById("container1");
    const container2 = document.getElementById("container2");

    // Remove forward animation
    container1.classList.remove("slide-left");

    // Slide register out to right
    container2.classList.remove("slide-in");
    container2.classList.add("offscreen-right");
  });

  // logic to check if the content in confirm password field matches the password field
  const registerPassword1 = document.getElementById("registerPassword1");
  const registerPassword2 = document.getElementById("registerPassword2");
  const validation = document.getElementById("validation");
  registerPassword2.addEventListener('input', () => {
    if (registerPassword1.value != registerPassword2.value) {
      validation.textContent = "Passwords don't match";
    } else {
      validation.textContent = "";
    }
  });

  // logic to check if the username already exists while registering a new user
  const registerBtn = document.getElementById("registerBtn");
  registerBtn.addEventListener("click", async (e) => {
    const registerUsername = document.getElementById("registerUsername").value;
    try {
      const checkUsername = await fetch(`${API_BASE}/api/auth/checkUsername/${registerUsername}`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });

      const data = await checkUsername.json();
      const value = data.data;
      if (value) throw new Exception(data.message);
    } catch (err) {
      const usernameFeedback = document.getElementById("usernameFeedback");
      usernameFeedback.textContent = err.message;
      usernameFeedback.style.display = 'inline';
    }
  });

  if (!form) return;
  const loginBtn = document.getElementById("loginBtn");
  const feedback = document.getElementById("authFeedback");

  loginBtn.addEventListener("click", async (e) => {
    e.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    errorEl.textContent = "";
    registerLink.style.display = "none";
    feedback.classList.remove("show");

    try {
      const res = await fetch(`${API_BASE}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: `{\"userName\": \"${username}\", \"password\": \"${password}\"}`
      });
      
      const data = await res.json();

      if (!res.ok || !data?.data) {
        throw new Error(data.message || "Invalid username or password");
      }

      const token = data.data;
      const decodedPayload = parseToken(token);
      const roles = decodedPayload.roles;

      // store the token
      localStorage.setItem("token", token);

      if (Object.values(roles).includes('ROLE_ADMIN')) {
        console.log("Welcome ADMIN")
        window.location.href = "adminDashboard.html";
      }

    } catch (error) {
      console.log('error message : ' + error);
      errorEl.textContent = errorMessage(error);
      registerLink.style.display = "block";
      feedback.classList.add("show");
    }
  });
});

function errorMessage (error) {
  return 'Unable to login';
}

function parseToken (token) {
  try {
      // Split the token into its parts (header.payload.signature)
      const parts = token.split('.');
      if (parts.length !== 3) {
          throw new Error('Invalid token format');
      }

      // The payload is the second part (index 1)
      const base64UrlPayload = parts[1];

      // Base64Url decode the payload. Note: standard atob() needs slight modification for base64url format
      const base64 = base64UrlPayload.replace(/-/g, '+').replace(/_/g, '/');
      const decodedPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));

      // Parse the JSON string into a JavaScript object
      return JSON.parse(decodedPayload);
  } catch (e) {
      console.error("Error decoding JWT:", e);
      return null;
  }
}

function returnQuote() {
  const quotes = [
    // Motivational
    "\"In football, the worst things are excuses. Excuses mean you cannot grow or move forward.\" — Pep Guardiola",
    "\"Success is no accident. It is hard work, perseverance, learning, studying, sacrifice, and most of all, love of what you are doing or learning to do.\" — Pelé",
    "\"If you want special results, you have to feel special things and do special things together. You can speak about spirit, or you can live it.\" — Jürgen Klopp",
    "\"Once you bid farewell to discipline, you say goodbye to success.\" — Sir Alex Ferguson",
    "\"If you think you're perfect already, then you never will be.\" — Cristiano Ronaldo",
    "\"It's hard to beat somebody that never gives up.\" — Megan Rapinoe",
    "\"The secret is to believe in your dreams; in your potential that you can be like your star, keep searching, keep believing and don't lose faith in yourself.\" — Neymar Jr.",
    "\"The fear of losing is sometimes greater than the will to win.\" — Emma Hayes",
    "\"Everybody has a talent, but it's what you do with it that makes it great.\" — Alex Morgan",
    "\"You have to fight to reach your dream. You have to sacrifice and work hard for it.\" — Lionel Messi",

    // Funny
    "\"Look, I'm a coach, I'm not Harry Potter. He is magical, but in reality, there is no magic. Magic is fiction and football is real.\" — José Mourinho",
    "\"Apparently, when you head a football, you lose brain cells, but it doesn't bother me. I'm a horse. No one's proved it yet have they?\" — David May",
    "\"I've had 14 bookings this season; eight of which were my fault, but seven of which were disputable.\" — Paul Gascoigne",
    "\"I couldn't settle in Italy; it was like living in a foreign country.\" — Ian Rush",
    "\"He cannot kick with his left foot, he cannot head a ball, he cannot tackle, and he doesn't score many goals. Apart from that he's alright.\" — George Best on David Beckham",
    "\"The World Cup is a truly international event.\" — John Motson",
    "\"Their football, Arsenal, is on another level, but Spurs are fighting like beavers.\" — Chris Kamara",
    "\"We didn't underestimate them, but they were a lot better than we thought.\" — Bobby Robson",
    "\"If history repeats itself, I should think we can expect the same thing again.\" — Terry Venables",
    "\"We must have had 99% of the game. It was the other three percent that cost us the match.\" — Ruud Gullit",

    // Teamwork & Perseverance
    "\"A team is not made up of isolated individuals. Always stay in the group. This way you have the strength of the group and you have the strength of the individual.\" — Diego Maradona",
    "\"No individual can win a game by himself.\" — Pelé",
    "\"Football is simple. But nothing is more difficult than playing simple football.\" — Johan Cruyff",
    "\"Geese always support each other. When a goose gets injured two birds always accompany it down to the ground. Just as geese do, we must support each other.\" — Emma Hayes",
    "\"No one is bigger than the team, you win together, and you can certainly lose together.\" — Sam Kerr",
    "\"Failure happens all the time. It happens every day in practice. What makes you better is how you react to it.\" — Mia Hamm",
    "\"A champion is someone who does not settle for that day's practice, that day's competition, that day's performance. They are always striving to be better. They don't live in the past.\" — Briana Scurry",
    "\"I start early and I stay late, day after day, year after year, it took me 17 years and 114 days to become an overnight success.\" — Lionel Messi",
    "\"The reason I succeed is because I've never been afraid to fail.\" — David Beckham",
    "\"Don't be scared to be ambitious. It's not a humiliation to have a high target and to fail. For me, the real humiliation is to have a target and not to give everything to reach it.\" — Arsène Wenger",

    // Bizarre
    "\"I pay for pizza, you pay for the sausage. I am the sausageman.\" — Claudio Ranieri",
    "\"Swedish style? No. Yugoslavian style? Of course not. It has to be Zlatan-style.\" — Zlatan Ibrahimovic",
    "\"I don't celebrate because I'm only doing my job. When a postman delivers letters, does he celebrate?\" — Mario Balotelli",
    "\"Some people cannot see a priest on a mountain of sugar.\" — Rafael Benitez",
    "\"Young players are a little bit like melons. Only when you open and taste the melon are you 100 per cent sure that the melon is good.\" — José Mourinho",
    "\"I was surprised, but I always say nothing surprises me in football.\" — Les Ferdinand",
    "\"Strangely, in slow motion replay, the ball seemed to hang in the air for even longer.\" — David Acfield",
    "\"In 1969, I gave up women and alcohol — it was the worst 20 minutes of my life.\" — George Best",
    "\"When the seagulls follow the trawler, it is because they think sardines will be thrown into the sea.\" — Eric Cantona",
    "\"I wouldn't say I was the best manager in the business. But I was in the top one.\" — Brian Clough",

    // Passion & Love
    "\"I'm living a dream I never want to wake up from.\" — Cristiano Ronaldo",
    "\"Football is an art like dancing is an art, but only when it's well done does it become an art.\" — Arsène Wenger",
    "\"Serious sport has nothing to do with fair play. It is bound up with hatred, jealousy, boastfulness, disregard of all rules and sadistic pleasure in witnessing violence: in other words, it is war minus the shooting.\" — George Orwell",
    "\"Football is a universal language. It brings people together, irrespective of their nationalities, cultures or beliefs.\" — George Weah",
    "\"Some people think football is a matter of life and death. I assure you, it's much more serious than that.\" — Bill Shankly",
    "\"Play for the name on the front of the shirt, and they will remember the name on the back.\" — Tony Adams",
    "\"Football is not just a game; it's an art form. You have to be creative, think outside the box, and always be one step ahead of your opponent.\" — Ronaldinho",
    "\"Football is the ballet of the masses.\" — Dmitri Shostakovich",
    "\"Football is a part of I. When I play, the world wakes up around me.\" — Bob Marley",
    "\"I don't believe skill was, or ever will be, the result of coaches. It is a result of a love affair between the child and the ball.\" — Roy Keane",
  ];

  const randomIndex = Math.floor(Math.random() * quotes.length);
  return quotes[randomIndex];
}