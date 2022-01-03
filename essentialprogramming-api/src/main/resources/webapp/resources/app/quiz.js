import {Utils} from "../js/utils";
import {docReady} from "../js/header";
import {loadComponent} from "../js/header";
import i18next from 'i18next';

let currentSlide = 0;
let submitButton, previousButton, nextButton;
let slides;
let questions;

window.onload = function () {

    let elements = document.getElementsByTagName('*');
    elements = [...elements].filter(element => element.hasAttribute && element.hasAttribute('data-include'));
    [...elements].forEach(loadComponent);


}

docReady(async function () {
    let lang = 'en'
    let urlLang = `../assets/i18n/${lang}.json`;
    let responseLang = await fetch(urlLang)
        .then(res => res.json())
        .then(json => {
            return json;
        })
        .catch(function (error) {
            console.log(error);
        });

    const initConfig = {
        lng: lang || 'en',
        debug: false,
        resources: {}
    };
    initConfig.resources[lang] = {
        translation: responseLang
    };
    await i18next.init(initConfig, function (err, t) {

    });

    let greet = i18next.t('greet');
    let a = i18next.t('title');
    let name = Utils.getRequestParameter("name");
    let userName = document.querySelector('.userName');
    sessionStorage.username = name != null ? name : "stranger";
    userName.innerText = greet + sessionStorage.username + " " + a;


    questions = transformJson(await getQuestions());

    // Kick things off
    buildQuiz(questions);

    slides = document.querySelectorAll(".slide");

    submitButton = document.getElementById('submit');
    previousButton = document.getElementById("previous");
    nextButton = document.getElementById("next");

    // Event listeners
    submitButton.addEventListener('click', showResults);
    previousButton.addEventListener("click", showPreviousSlide);
    nextButton.addEventListener("click", showNextSlide);


    // Show the first slide
    showSlide(currentSlide);


});

// Functions
function buildQuiz(questions) {
    // variable to store the HTML output
    const output = [];

    // for each question...
    questions.forEach(
        (currentQuestion, questionNumber) => {

            // variable to store the list of possible answers
            const answers = [];

            // and for each available answer...
            for (let letter in currentQuestion.answers) {

                // ...add an HTML radio button
                answers.push(
                    `<label>
              <input type="radio" name="question${questionNumber}" value="${letter}">
              ${letter} :
              ${currentQuestion.answers[letter]}
            </label>`
                );
            }

            // add this question and its answers to the output
            output.push(
                `<div class="slide">
            <div class="question"> ${currentQuestion.question} </div>
            <div class="answers"> ${answers.join("")} </div>
          </div>`
            );
        }
    );

    // finally combine our output list into one string of HTML and put it on the page
    const quizContainer = document.getElementById('quiz');
    quizContainer.innerHTML = output.join('');
}

function showResults() {

    let result = {
        quizId: 0,
        userAnswers: []
    };

    // gather answer containers from our quiz
    const quizContainer = document.getElementById('quiz');
    const answerContainers = quizContainer.querySelectorAll('.answers');

    // keep track of user's answers
    let numCorrect = 0;

    // for each question...
    questions.forEach((currentQuestion, questionNumber) => {

        // find selected answer
        const answerContainer = answerContainers[questionNumber];
        const selector = `input[name=question${questionNumber}]:checked`;
        const userAnswer = (answerContainer.querySelector(selector) || {}).value;

        // if answer is correct
        if (userAnswer === currentQuestion.correctAnswer) {
            // add to the number of correct answers
            numCorrect++;

            // color the answers green
            answerContainers[questionNumber].style.color = 'lightgreen';
        }
        // if answer is wrong or blank
        else {
            // color the answers red
            answerContainers[questionNumber].style.color = 'red';
        }
        let item = {
            questionId: currentQuestion.id,
            answer: userAnswer
        }
        result.userAnswers.push(item);

    });

    // show number of correct answers out of total
    const resultsContainer = document.getElementById('results');
    resultsContainer.innerHTML = `${numCorrect} out of ${questions.length}`;
}


function showSlide(n) {

    slides[currentSlide].classList.remove('active-slide');
    slides[n].classList.add('active-slide');
    currentSlide = n;
    if (currentSlide === 0) {
        previousButton.style.display = 'none';
    } else {
        previousButton.style.display = 'inline-block';
    }
    if (currentSlide === slides.length - 1) {
        nextButton.style.display = 'none';
        submitButton.style.display = 'inline-block';
    } else {
        nextButton.style.display = 'inline-block';
        submitButton.style.display = 'none';
    }
}

function showNextSlide() {
    showSlide(currentSlide + 1);
}

function showPreviousSlide() {
    showSlide(currentSlide - 1);
}

function transformJson(questions) {
    const alphabet = ['a', 'b', 'c', 'd', 'e'];
    const json = questions.map((question, index) => (
        {
            question: question.question,
            answers: Object.assign(
                {},
                ...question.answers.map((a, i) => ({[alphabet[i]]: a}))
            ),
            correctAnswer: question.correctAnswer
        }
    ));
    console.log(JSON.stringify(json, null, 2));
    return json;
}

async function getQuestions() {

    let name = Utils.getRequestParameter("name");
    let url = `/api/questions?name=${name}`;
    let response = await fetch(url)
        .then(res => res.json())
        .then(json => {
            return json;

        })
        .catch(function (error) {
            console.log(error);
        });

    console.log(response)
    return response;
}
