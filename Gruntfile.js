module.exports = function(grunt) {

	// Project configuration.
	grunt.initConfig({
		pkg : grunt.file.readJSON('package.json'),

		cssmin: {
		  combine: {
		    files: {
		      'WebRoot/dist/css/common/master.min.css': ['WebRoot/css/common/normalize.css', 'WebRoot/css/common/global.css']
		    }
		  },
		  build: { 
			files: [{
				expand: true,
				cwd: 'WebRoot/css/common',
				src: ['*.css', '!*.min.css'],
				dest: 'WebRoot/dist/css/dist/',
				ext: '.min.css'
			}]
		 }
		},

		copy:{
			main: {
    			files: [
      				{
      					expand: true,
						dot: true,
						cwd: 'WebRoot/css/img',
						dest: 'WebRoot/dist/css/img',
      					src: ['*']}, 
    			]
  			}
		},

		requirejs: {
			options: {
				baseUrl: "WebRoot",
				mainConfigFile : "WebRoot/js/common/require_config.js",
				// optimize:"uglify"
				// dir: "js/controller"
				
			},
			build: {
				options:
					{
						include: ["js/controller/index"],
						out: "WebRoot/dist/js/build/index.js"
					}
				
			}

			 // uglify: {
    //     toplevel: true,
    //     ascii_only: true,
    //     beautify: true,
    //     // max_line_length: 1000,

    //     //How to pass uglifyjs defined symbols for AST symbol replacement,
    //     //see "defines" options for ast_mangle in the uglifys docs.
    //     defines: {
    //         DEBUG: ['name', 'false']
    //     },

    //     //Custom value supported by r.js but done differently
    //     //in uglifyjs directly:
    //     //Skip the processor.ast_mangle() part of the uglify call (r.js 2.0.5+)
    //     no_mangle: true
    // },
			// index: { options: { include: ["js/controller/index"],  out: "WebRoot/dist/js//build/index.js" } },
			// about: { options: { include: ["js/controller/about"],  out: "./WebRoot/js/controller/built/about-built.js" } },
			// question: { options: { include: ["js/controller/question"],  out: "./WebRoot/js/controller/built/question-built.js" } },
			// agreement: { options: { include: ["js/controller/agreement"],  out: "./WebRoot/js/controller/built/agreement-built.js" } },
			// error: { options: { include: ["js/controller/error"],  out: "./WebRoot/js/controller/built/error-built.js" } },
			// bidforward: { options: { include: ["js/controller/bidforward"],  out: "./WebRoot/js/controller/built/bidforward-built.js" } },
			// signin: { options: { include: ["js/controller/signin"],  out: "./WebRoot/js/controller/built/signin-built.js" } },
			// signup: { options: { include: ["js/controller/signup"],  out: "./WebRoot/js/controller/built/signup-built.js" } },
			// upwd: { options: { include: ["js/controller/upwd"],  out: "./WebRoot/js/controller/built/upwd-built.js" } },
			// usetting: { options: { include: ["js/controller/usetting"],  out: "./WebRoot/js/controller/built/usetting-built.js" } },
			// mailbox: { options: { include: ["js/controller/mailbox"],  out: "./WebRoot/js/controller/built/mailbox-built.js" } },
			// mailresend: { options: { include: ["js/controller/mailresend"],  out: "./WebRoot/js/controller/built/mailresend-built.js" } },
			// placepool: { options: { include: ["js/controller/placepool"],  out: "./WebRoot/js/controller/built/placepool-built.js" } },
			// placebid: { options: { include: ["js/controller/placebid"],  out: "./WebRoot/js/controller/built/placebid-built.js" } },
			// jobinvite: { options: { include: ["js/controller/jobinvite"],  out: "./WebRoot/js/controller/built/jobinvite-built.js" } },
			// jobintent: { options: { include: ["js/controller/jobintent"],  out: "./WebRoot/js/controller/built/jobintent-built.js" } },
			// jobinfo: { options: { include: ["js/controller/jobinfo"],  out: "./WebRoot/js/controller/built/jobinfo-built.js" } },
			// jobresult: { options: { include: ["js/controller/jobresult"],  out: "./WebRoot/js/controller/built/jobresult-built.js" } },
			// jobbidlist: { options: { include: ["js/controller/jobbidlist"],  out: "./WebRoot/js/controller/built/jobbidlist-built.js" } },
			// complist: { options: { include: ["js/controller/complist"],  out: "./WebRoot/js/controller/built/complist-built.js" } },
			// compslt: { options: { include: ["js/controller/compslt"],  out: "./WebRoot/js/controller/built/compslt-built.js" } },
			// compinfo: { options: { include: ["js/controller/compinfo"],  out: "./WebRoot/js/controller/built/compinfo-built.js" } },
			// compdefault: { options: { include: ["js/controller/compdefault"],  out: "./WebRoot/js/controller/built/compdefault-built.js" } },
			// compbidlist: { options: { include: ["js/controller/compbidlist"],  out: "./WebRoot/js/controller/built/compbidlist-built.js" } },
			// compindex: { options: { include: ["js/controller/compindex"],  out: "./WebRoot/js/controller/built/compindex-built.js" } }
		},

		// uglify : {
		// 	// options : {
		// 		//banner : '/*! ver 0.0.1 */\n',
		// 		// report: "gzip",
		// 		// beautify : false,
		// 		// mangle : true,
		// 		// compress : {
		// 		// 	global_defs : {
		// 		// 		"DEBUG" : false
		// 		// 	},
		// 		// 	drop_console : true
		// 		// },
		// 		build : {
		// 			src : 'WebRoot/js/controller/*.js',
		// 			dest : 'WebRoot/js/controller/*.min.js'
		// 		}
		// 	// },
		// 	dist : {
		// 		files : [ {
		// 			expand : true,
		// 			cwd : 'WebRoot/dist/js/build',
		// 			src : '*.js',
		// 			dest : 'WebRoot/dist/js',
		// 			ext : '.min.js'
		// 		} ]
		// 	}
		// },
		
		
		clean:{
			dist: ['WebRoot/dist']
		},
		
		// jshint: {
		// 	options: {
		// 		jshintrc: ".jshintrc",
		// 		force: false
		// 	},
		// 	files: ['Gruntfile.js', 'WebRoot/js/**/*.js']
		// },
		

	    // ngtemplates: {
	    //   dist: {
	    //     options: {
	    //       module: 'firstNgAppApp',
	    //       htmlmin: '<%= htmlmin.dist.options %>',
	    //       usemin: 'scripts/scripts.js'
	    //     },
	    //     cwd: '<%= yeoman.app %>',
	    //     src: 'views/{,*/}*.html',
	    //     dest: '.tmp/templateCache.js'
	    //   }
	    // },
		// watch:{
		// 	compile:{
		// 		files: ['WebRoot/js/controller/built/*-built.js','WebRoot/css/common/*.css', 'WebRoot/css/common/!*.min.css'],
		// 		tasks: ['uglify', 'cssmin']
		// 	}
		// }
		// htmlmin: {
		 //      dist: {
		 //        options: {
		 //          collapseWhitespace: true,
		 //          conservativeCollapse: true,
		 //          collapseBooleanAttributes: true,
		 //          removeCommentsFromCDATA: true
		 //        },
		 //        files: [{
		 //          expand: true,
		 //          cwd: '<%= yeoman.dist %>',
		 //          src: ['*.html'],
		 //          dest: '<%= yeoman.dist %>'
		 //        }]
		 //      }
		 //    },
		 // imagemin: {
		 //      dist: {
		 //        files: [{
		 //          expand: true,
		 //          cwd: '<%= yeoman.app %>/images',
		 //          src: '{,*/}*.{png,jpg,jpeg,gif}',
		 //          dest: '<%= yeoman.dist %>/images'
		 //        }]
	  //     		}
	  //   	},
	});

	// grunt.loadNpmTasks('grunt-contrib-jshint');
	grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-cssmin');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-contrib-requirejs');
	grunt.loadNpmTasks('grunt-contrib-uglify');

	// 默认被执行的任务列表。
	grunt.registerTask('default', [ 
        // 'jshint',
        'clean',
        'cssmin',
        'copy',
        'requirejs',
        'uglify'
        
        
    ]);
	
	/*grunt.registerTask('validate', [
		'jshint'
	]);*/
};